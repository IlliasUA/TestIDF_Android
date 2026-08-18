#!/usr/bin/env python3
"""Convert the Android Kotlin question bank and XML descriptions to iOS JSON."""

from __future__ import annotations

import argparse
import json
import re
import xml.etree.ElementTree as ET
from pathlib import Path


CATEGORIES = (
    ("Test_Data.kt", "tanks", "Chars", "tank_images"),
    ("Art_Data.kt", "artillery", "Artillerie", "artillery_images"),
    ("Air_Data.kt", "aviation", "Aviation", "air_images"),
    ("Genie_Data.kt", "engineering", "Génie", "genie_images"),
    ("Recon_Data.kt", "reconnaissance", "Reconnaissance", "recon_images"),
    ("Test_bm2.kt", "militaryVehicles", "Militaire", "bm2_images"),
)

LOCALES = {
    "fr": "values",
    "en": "values-en",
    "es": "values-es",
    "pt": "values-pt",
    "zh-Hans": "values-cn",
}


def kotlin_strings(source: str) -> list[str]:
    return [json.loads(f'"{value}"') for value in re.findall(r'"((?:\\.|[^"\\])*)"', source)]


def field_string(block: str, name: str) -> str:
    match = re.search(rf"\b{name}\s*=\s*(\"(?:\\.|[^\"\\])*\")", block)
    if not match:
        raise ValueError(f"Missing {name} in {block[:100]!r}")
    return json.loads(match.group(1))


def field_list(block: str, name: str) -> list[str]:
    match = re.search(rf"\b{name}\s*=\s*listOf\((.*?)\)", block, re.DOTALL)
    return kotlin_strings(match.group(1)) if match else []


def resource_name(block: str, name: str) -> str | None:
    match = re.search(rf"\b{name}\s*=\s*(?:legOS\.testidf\.)?R\.string\.([A-Za-z0-9_]+)", block)
    return match.group(1) if match else None


def question_blocks(source: str) -> list[str]:
    blocks: list[str] = []
    cursor = 0
    while True:
        start = source.find("Question(", cursor)
        if start < 0:
            return blocks
        depth = 0
        in_string = False
        escaped = False
        for index in range(start + len("Question("), len(source)):
            character = source[index]
            if in_string:
                if escaped:
                    escaped = False
                elif character == "\\":
                    escaped = True
                elif character == '"':
                    in_string = False
                continue
            if character == '"':
                in_string = True
            elif character == "(":
                depth += 1
            elif character == ")":
                if depth == 0:
                    blocks.append(source[start : index + 1])
                    cursor = index + 1
                    break
                depth -= 1
        else:
            raise ValueError(f"Unclosed Question block at offset {start}")


def load_translations(res_root: Path) -> dict[str, dict[str, str]]:
    result: dict[str, dict[str, str]] = {}
    for locale, directory in LOCALES.items():
        for xml_path in sorted((res_root / directory).glob("*.xml")):
            root = ET.parse(xml_path).getroot()
            for node in root.findall("string"):
                name = node.attrib.get("name")
                if not name:
                    continue
                value = "".join(node.itertext()).replace("\\'", "'").strip()
                result.setdefault(name, {})[locale] = value
    return result


def build_catalog(android_root: Path) -> dict[str, object]:
    kotlin_root = android_root / "app/src/main/java/com/example/quizapp"
    assets_root = android_root / "app/src/main/assets"
    translations = load_translations(android_root / "app/src/main/res")
    questions: list[dict[str, object]] = []

    for filename, ios_category, firestore_category, image_folder in CATEGORIES:
        source = (kotlin_root / filename).read_text(encoding="utf-8")
        for position, block in enumerate(question_blocks(source), start=1):
            image_name = field_string(block, "image")
            additional_images = field_list(block, "additionalImages")
            options = field_list(block, "options")
            correct_answer = field_string(block, "correct")
            description_id = resource_name(block, "descriptionResId")
            images = [image_name, *additional_images]

            missing_images = [
                name for name in images if not (assets_root / image_folder / name).is_file()
            ]
            if missing_images:
                raise ValueError(f"{filename} question {position}: missing images {missing_images}")
            if correct_answer not in options:
                raise ValueError(f"{filename} question {position}: correct answer not in options")

            questions.append(
                {
                    "id": f"{ios_category}-{position:03d}",
                    "category": ios_category,
                    "legacyCategory": firestore_category,
                    "imageFolder": image_folder,
                    "imageName": image_name,
                    "additionalImages": additional_images,
                    "correctAnswer": correct_answer,
                    "options": options,
                    "descriptions": translations.get(description_id, {}) if description_id else {},
                }
            )

    return {
        "schemaVersion": 1,
        "questionCount": len(questions),
        "questions": questions,
    }


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--android-root", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    arguments = parser.parse_args()

    catalog = build_catalog(arguments.android_root.resolve())
    arguments.output.parent.mkdir(parents=True, exist_ok=True)
    arguments.output.write_text(
        json.dumps(catalog, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(f"Wrote {catalog['questionCount']} questions to {arguments.output}")


if __name__ == "__main__":
    main()
