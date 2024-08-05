import json


def eprint(*args, **kwargs):
    import sys
    print(*args, file=sys.stderr, **kwargs)


def block(name: str):
    return {
        "Name": f"minecraft:{name}",
        # "Properties": {}
    }


def recipe(ingredients: [str], result: str, name: str = "", prefix: str = ""):
    if name == "":
        name = result

    if prefix != "":
        name = f"{prefix}_{name}"

    return (name, {
        "type": "superflat-plus:anvil_drop",
        "blocks": [block(x) for x in ingredients],
        "result": block(result)
    })


recipes = [
    recipe(["infested_stone_bricks"], "infested_cracked_stone_bricks"),
    recipe(["nether_bricks"], "cracked_nether_bricks"),

    recipe(["diamond_block", "deepslate"], "deepslate_diamond_ore", prefix=""),
    recipe(["diamond_block", "stone"], "diamond_ore", prefix=""),

    recipe(["coal_block", "deepslate"], "deepslate_coal_ore", prefix=""),
    recipe(["coal_block", "stone"], "coal_ore", prefix=""),
    recipe(["redstone_block", "deepslate"], "redstone_coal_ore", prefix=""),
    recipe(["redstone_block", "stone"], "redstone_ore", prefix=""),
    recipe(["lapis_block", "deepslate"], "deepslate_lapis_ore", prefix=""),
    recipe(["lapis_block", "stone"], "lapis_ore", prefix=""),
    recipe(["gold_block", "stone"], "gold_ore", prefix=""),
    recipe(["gold_block", "netherrack"], "nether_gold_ore", prefix=""),
    recipe(["gold_block", "blackstone"], "gilded_blackstone", prefix=""),

    recipe(["gold_block", "deepslate"], "deepslate_gold_ore", prefix=""),
    recipe(["raw_gold_block", "deepslate"],
           "deepslate_gold_ore", prefix="raw"),

    recipe(["raw_gold_block", "stone"], "gold_ore", prefix="raw"),
    recipe(["raw_gold_block", "netherrack"], "nether_gold_ore", prefix="raw"),
    recipe(["raw_gold_block", "blackstone"],
           "gilded_blackstone", prefix="raw"),
    recipe(["copper_block", "deepslate"], "deepslate_copper_ore", prefix=""),
    recipe(["copper_block", "stone"], "copper_ore", prefix=""),
    recipe(["raw_copper_block", "deepslate"],

           "deepslate_copper_ore", prefix="raw"),
    recipe(["raw_copper_block", "stone"], "copper_ore", prefix="raw"),
    recipe(["iron_block", "deepslate"], "deepslate_iron_ore", prefix=""),
    recipe(["iron_block", "stone"], "iron_ore", prefix=""),

    recipe(["raw_iron_block", "deepslate"],
           "deepslate_iron_ore", prefix="raw"),
    recipe(["raw_iron_block", "stone"], "iron_ore", prefix="raw"),

    recipe(["emerald_block", "deepslate"], "deepslate_emerald_ore", prefix=""),
    recipe(["emerald_block", "stone"], "emerald_ore", prefix=""),
    recipe(["quartz_block", "netherrack"], "nether_quartz_ore", prefix=""),
    recipe(["stone_bricks"], "cracked_stone_bricks", prefix=""),
    recipe(["deepslate_bricks"], "cracked_deepslate_bricks", prefix=""),
    recipe(["deepslate_tiles"], "cracked_deepslate_tiles", prefix=""),
    recipe(["polished_blackstone_bricks"],
           "cracked_polished_blackstone_bricks", prefix=""),
    recipe(["polished_granite"], "granite", prefix=""),
    recipe(["polished_andesite"], "andesite", prefix=""),
    recipe(["polished_diorite"], "diorite", prefix=""),
    recipe(["chiseled_stone_bricks"], "stone_bricks", prefix=""),
    recipe(["polished_deepslate"], "deepslate",
           prefix="polished_deepslate_to"),
    recipe(["chiseled_deepslate"], "deepslate",
           prefix="chiseled_deepslate_to"),

    recipe(["chiseled_sandstone"], "sandstone", prefix="chiseled_sandstone"),
    recipe(["smooth_sandstone"], "sandstone", prefix="smooth_sandstone"),
    recipe(["cut_sandstone"], "sandstone", prefix="cut_sandstone"),

    recipe(["chiseled_red_sandstone"], "sandstone",
           prefix="chiseled_red_sandstone"),
    recipe(["smooth_red_sandstone"], "sandstone",
           prefix="smooth_red_sandstone"),
    recipe(["cut_red_sandstone"], "sandstone", prefix="cut_red_sandstone"),

    recipe(["chiseled_nether_bricks"], "nether_bricks", prefix=""),
    recipe(["smooth_basalt"], "basalt", prefix=""),
    recipe(["chiseled_polished_blackstone"], "polished_blackstone", prefix=""),
    recipe(["polished_blackstone"], "blackstone", prefix=""),

    recipe(["chiseled_quartz_block"], "quartz_block", prefix="chiseled_quartz"),
    recipe(["quartz_bricks"], "quartz_block", prefix="quartz_bricks"),
    recipe(["quartz_pillar"], "quartz_block", prefix="quartz_pillar"),
    recipe(["smooth_quartz"], "quartz_block", prefix="smooth_quartz"),

    recipe(["quartz_block", "diorite"], "calcite", prefix="1"),
    recipe(["diorite", "quartz_block"], "calcite", prefix="2"),

    recipe(["cobbled_deepslate", "cobblestone"], "tuff", prefix="1"),
    recipe(["cobblestone", "cobbled_deepslate"], "tuff", prefix="2"),
    recipe(["redstone_lamp"], "glowstone", prefix=""),
]


# concrete
for color in [
    "red",
    "lime",
    "pink",
    "gray",
    "cyan",
    "blue",
    "white",
    "brown",
    "green",
    "black",
    "orange",
    "yellow",
    "purple",
    "magenta",
    "light_blue",
    "light_gray",
]:
    recipes.append(recipe([f"{color}_concrete"], f"{color}_concrete_powder"))
    recipes.append(
        recipe([f"{color}_glazed_terracotta"], f"{color}_glazed_terracotta"))

written_files = []

for (name, r) in recipes:
    if name == "":
        continue

    filename = f"{name}_anvil.json"

    if filename in written_files:
        eprint(f"Duplicate recipes with name: '{filename}'")

    # print(f"Writing '{filename}'")

    written_files.append(filename)

    with open(filename, "w") as file:
        json.dump(r, file)
