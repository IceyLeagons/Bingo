package net.iceyleagons.bingo.texture;

import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum MaterialTexture {

    AIR, STONE(Difficulty.EASY),
    GRANITE(Difficulty.EASY), POLISHED_GRANITE(Difficulty.EASY),
    DIORITE(Difficulty.EASY), POLISHED_DIORITE(Difficulty.EASY),
    ANDESITE(Difficulty.EASY), POLISHED_ANDESITE(Difficulty.EASY),
    SNOWY_GRASS, GRASS_BLOCK, TOPSIDE_GRASS,
    DIRT(Difficulty.FREE), COARSED_DIRT(Difficulty.HARD),
    PODZOL, TOPSIDE_PODZOL,
    COBBLESTONE(Difficulty.FREE), OAK_PLANKS(Difficulty.FREE),
    SPRUCE_PLANKS, BIRCH_PLANKS(Difficulty.EASY),
    JUNGLE_PLANKS, ACACIA_PLANKS,
    DARK_OAK_PLANKS, OAK_SAPLING(Difficulty.EASY),
    SPRUCE_SAPLING, BIRCH_SAPLING(Difficulty.EASY),
    JUNGLE_SAPLING, ACACIA_SAPLING,
    DARK_OAK_SAPLING, BEDROCK, FLOWING_WATER, FLOWING_WATER2,
    FLOWING_LAVA, FLOWING_LAVA2, SAND(Difficulty.FREE), RED_SAND(Difficulty.EXPERT),
    GRAVEL(Difficulty.FREE), GOLD_ORE(Difficulty.MEDIUM), IRON_ORE(Difficulty.EASY),
    COAL_ORE, OAK_LOG(Difficulty.FREE), I, SPRUCE_LOG,
    G, BIRCH_LOG(Difficulty.EASY), N, JUNGLE_LOG, O, ACACIA_LOG,
    R, DARK_OAK_LOG, E, STRIPPED_OAK_LOG(Difficulty.FREE), D,
    STRIPPED_SPRUCE_LOG, IG, STRIPPED_BIRCH_LOG(Difficulty.FREE),
    IGN, STRIPPED_JUNGLE_LOG, IGNO, STRIPPED_ACACIA_LOG,
    IGNOR, STRIPPED_DARK_OAK_LOG, IGNORE, OAK_LEAVES(Difficulty.EASY),
    SPRUCE_LEAVES, BIRCH_LEAVES, JUNGLE_LEAVES,
    ACACIA_LEAVES, DARK_OAK_LEAVES, SPONGE, WET_SPONGE,
    GLASS(Difficulty.EASY), LAPIS_LAZULI_ORE(Difficulty.MEDIUM), LAPIS_LAZULI_BLOCK(Difficulty.MEDIUM),
    DISPENSER(Difficulty.HARD), DISPENSER_FACING_UP, DISPENSER_SIDE, DISPENSER_BACK,
    SANDSTONE(Difficulty.EASY), SMOOTH_SANDSTONE(Difficulty.MEDIUM), SANDSTONE_DOWN,
    CHISELED_SANDSTONE(Difficulty.MEDIUM), CUT_SANDSTONE(Difficulty.MEDIUM), NOTEBLOCK(Difficulty.MEDIUM),
    B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12, B13, B14, B15, B16, B17, B18,
    B19, B20, B21, B22, B23, B24, B25, B26, B27, B28, B29, B30, B31, B32, B33, B34,
    B35, B36, B37, B38, B39, B40, B41, B42, B43, B44, B45, B46, B47, B48, B49, B50,
    B51, B52, B53, B54, B55, B56, B67, B58, B59, B60, B61, B62, B63, B64, R1, R2, R3,
    POWERED_RAIL(Difficulty.HARD), R4, R5, R6, DETECTOR_RAIL(Difficulty.HARD), P1, P2, P3, P4,
    P5, P6, P7, P8, P9, P10, P11, P12, STICKY_PISTON, P13, P14, COBWEB,
    GRASS(Difficulty.MEDIUM), FERN(Difficulty.MEDIUM), DEAD_BUSH(Difficulty.MEDIUM),
    SEA_GRASS(Difficulty.MEDIUM), G1, G2, P15, P16, P17, P18, P19, P20, P22, P23, P24, P25, P26, P27,
    PISTON(Difficulty.MEDIUM), P28, P29, WHITE_WOOL(Difficulty.EASY), ORANGE_WOOL(Difficulty.MEDIUM),
    MAGENTA_WOOL(Difficulty.MEDIUM), LIGHT_BLUE_WOOL(Difficulty.MEDIUM), YELLOW_WOOL(Difficulty.MEDIUM),
    LIME_WOOL(Difficulty.MEDIUM), PINK_WOOL(Difficulty.EXPERT), GRAY_WOOL(Difficulty.MEDIUM),
    LIGHT_GRAY_WOOL(Difficulty.MEDIUM), CYAN_WOOL(Difficulty.MEDIUM), PURPLE_WOOL(Difficulty.HARD),
    BLUE_WOOL(Difficulty.MEDIUM), BROWN_WOOL(Difficulty.MEDIUM), GREEN_WOOL(Difficulty.MEDIUM),
    RED_WOOL(Difficulty.MEDIUM), BLACK_WOOL(Difficulty.EASY), DANDELION(Difficulty.FREE), ROSE(Difficulty.FREE),
    BLUE_ORCHID(Difficulty.MEDIUM), ALLIUM(Difficulty.MEDIUM), AZURE_BLUET(Difficulty.MEDIUM),
    RED_TULIP(Difficulty.MEDIUM), ORANGE_TULIP(Difficulty.MEDIUM), WHITE_TULIP(Difficulty.MEDIUM),
    PINK_TULIP(Difficulty.MEDIUM), RANDOM_FLOWER, BROWN_MUSHROOM(Difficulty.EASY), RED_MUSHROOM(Difficulty.EASY),
    GOLD_BLOCK(Difficulty.HARD), IRON_BLOCK(Difficulty.MEDIUM), BRICKS(Difficulty.EASY), TNT(Difficulty.MEDIUM),
    T1, T2, BOOKSHELF(Difficulty.MEDIUM), MOSS_COBBLESTONE(Difficulty.HARD), OBSIDIAN(Difficulty.HARD),
    T3, FIRE, FIRE2, MOB_SPAWNER, OAK_STAIRS(Difficulty.EASY), S1, S2, S3, CHEST(Difficulty.EASY),
    C1, C2, C3, C4, C5, RE1, RE2, RE3, RE4, RE5, RE6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20,
    R21, R22, R23, R24, R25, R26, R27, R28, R29, R30, R31, R32, R33, R34, R35, R36, R37, R38, R39, R40,
    R41, R42, R43, R44, R45, R46, R47, R48, DIAMOND_ORE, DIAMOND_BLOCK(Difficulty.EXPERT),
    WORKBENCH(Difficulty.FREE), CRAFTING_TABLE(Difficulty.FREE), C6, CR1, CR2, CR3, CR4, CR5, CR6, CR7, CR8,
    F1, F2, F3, F4, F5, FURNACE(Difficulty.FREE), S4, D1, D2, LADDER(Difficulty.EASY), RA1, RAIL(Difficulty.MEDIUM),
    RA2, RA3, RA4, RA5, COBBLESTONE_STAIRS(Difficulty.EASY), S5, S6, S7, WALL_SIGN, LEVER(Difficulty.EASY),
    STONE_PRESSURE_PLATE, D3, D4, OAK_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE,
    BIRCH_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, ACACIA_PRESSURE_PLATE,
    DARK_OAK_PRESSURE_PLATE, REDSTONE_ORE(Difficulty.EXPERT), T4, T5, STONE_BUTTON(Difficulty.MEDIUM),
    SN1, SN2, SN3, SN4, SN5, SN6, SN7, SN8, ICE(Difficulty.EXPERT), CACTUS(Difficulty.MEDIUM), CA, CAC, CLAY_BLOCK(Difficulty.EASY),
    SUGARCANE, JUKEBOX(Difficulty.HARD), JUKE, F, PUMPKIN(Difficulty.MEDIUM), P,
    NETHERRACK(Difficulty.MEDIUM), SOUL_SAND(Difficulty.MEDIUM), GLOWSTONE(Difficulty.MEDIUM), PORTAL, CARVED_PUMPKIN(Difficulty.MEDIUM),
    JACK_O_LANTERN(Difficulty.MEDIUM), CAK, CAKE_BLOCK, CACK, CAK1, CAK2, CAK3, CAK4, CAK5,
    CAK6, CAK7, CAK8, CAK9, CAK10, CAK11, CAK12, CAK13, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20,
    A21, A22, A23, A24, A25, A26, A27, A28, A29, A30, A31, A32, A33, A34, A35, A36, A37, A38, A39, A40, A41, A42, A43, A44, A45, A46, A47, A48, A49, A50,
    A51, A52, A53, A54, A55, A56, A57, A58, A59, A60, A61, A62, A63, A64, A65, A66, A67, A68, A69, A70, A71, A72, A73, A74, A75, A76, A77, A78, A79, A80,
    A81, A82, A83, A84, A85, A86, A87, A88, A89, A90, A91, A92, A93, A94, A95, A96,
    WHITE_STAINED_GLASS(Difficulty.HARD), ORANGE_STAINED_GLASS(Difficulty.HARD), MAGENTA_STAINED_GLASS(Difficulty.HARD),
    LIGHT_BLUE_STAINED_GLASS(Difficulty.HARD), YELLOW_STAINED_GLASS(Difficulty.HARD), LIME_STAINED_GLASS(Difficulty.HARD),
    PINK_STAINED_GLASS(Difficulty.EXPERT), GRAY_STAINED_GLASS(Difficulty.HARD), LIGHT_GRAY_STAINED_GLASS(Difficulty.HARD),
    CYAN_STAINED_GLASS(Difficulty.HARD), PURPLE_STAINED_GLASS(Difficulty.HARD), BLUE_STAINED_GLASS(Difficulty.HARD),
    BROWN_STAINED_GLASS(Difficulty.HARD), GREEN_STAINED_GLASS(Difficulty.HARD), RED_STAINED_GLASS(Difficulty.HARD),
    BLACK_STAINED_GLASS(Difficulty.HARD), OAK_TRAPDOOR(Difficulty.EASY), TRAPDOOR, SPRUCE_TRAPDOOR,
    STRAPDOOR, BIRCH_TRAPDOOR(Difficulty.EASY), BTRAPDOOR, JUNGLE_TRAPDOOR, JTRAPDOOR,
    ACACIA_TRAPDOOR, ATRAPDOOR, DARK_OAK_TRAPDOOR, DOTRAPDOOR,
    INFS, INFC, STONE_BRICK(Difficulty.MEDIUM), MOSSY_STONE_BRICK(Difficulty.EXPERT), CRACKED_STONE_BRICK(Difficulty.EXPERT),
    CHISELED_STONE_BRICK(Difficulty.HARD), INFB, INFMB, INFCB, INFCHB, M1, M2, M3, M4, IRON_BARS(Difficulty.MEDIUM), GLASS_PANE,
    MELON_BLOCK, MB, MB1, MB2, MB3, MB4, MB5, MB6, MB7, MB8, MB9, MB10, MB11, MB12, MB13, MB14, MB15, MB16, MB17, MB18,
    VINE(Difficulty.EXPERT), OAK_FENCE_GATE(Difficulty.EASY), BRICK_STAIRS(Difficulty.MEDIUM), BS1, BS2, BS3, STONE_BRICK_STAIRS(Difficulty.MEDIUM),
    SB1, SB2, SB3, MYCELIUM, MYCEL_TOP, LILYPAD(Difficulty.HARD), NETHER_BRICK(Difficulty.MEDIUM), NETHER_BRICK_FENCE(Difficulty.EXPERT),
    NETHER_BRICK_STAIRS(Difficulty.MEDIUM), NB1, NB2, NB3, NW1, NETHER_WART(Difficulty.EXPERT), NW2, ENCH_BOTTOM, ENCHANTMENT_TABLE(Difficulty.HARD),
    ENCH_TOP, ENCH_TOP_BOOK, ENCH_BOOK, ENCH_BOOK_BOTTOM, BREWING_STAND, BS, CAULDRON(Difficulty.MEDIUM), CA1, CA2, CA3, NOTHINGNESS,
    END1, END2, END3, END4, END5, END6, END7, END8, END9, END10, END_STONE, DRAGON_EGG, REDSTONE_LAMP_LIT,
    REDSTONE_LAMP(Difficulty.MEDIUM), CO1, CO2, CO3, CO4, CO5, CO6, SANDSTONE_STAIRS(Difficulty.MEDIUM), SS1, SS2, SS3,
    EMERALD_ORE, ENDER_CHEST, TW1, TW2, TW3, TW4, TW5, TRIPWIRE_HOOK(Difficulty.HARD), ST1, ST2, ST3,
    EMERALD_BLOCK, SPRUCE_STAIRS, SPS1, SPS2, SPS3, BIRCH_STAIRS(Difficulty.EASY), BHS1, BHS2, BHS3,
    JUNGLE_STAIRS(Difficulty.HARD), JS1, JS2, JS3, CMD1, CMD2, CMD3, CMD4, CMD5, CMD6, CMD7, CMD8, CMD9, CMD10, BEACON_TOP, BEACON, BEACON_BOTTOM,
    COBBLESTONE_WALL(Difficulty.MEDIUM), MOSSY_COBBLESTONE_WALL, FLOWER_POT(Difficulty.MEDIUM), FLOWER_POT_TOP, CRO1, CRO2, CRO3, CRO4, CRO5, CRO6, CRO7, CRO8,
    OAK_BUTTON(Difficulty.EASY), SPRUCE_BUTTON, BIRCH_BUTTON(Difficulty.EASY), JUNGLE_BUTTON, ACACIA_BUTTON,
    DARK_OAK_BUTTON, SKELETON_SKULL, WITHER_SKELETON_SKULL(Difficulty.EXPERT), ZOMBIE_SKULL, PLAYER_SKULL, CREEPER_SKULL, ENDER_DRAGON_HEAD,
    ANVIL(Difficulty.HARD), ANVIL_TOP, ANVIL_TOP_SLIGHTLY_BROKEN, ANVIL_TOP_BROKEN, TRAPPED_CHEST(Difficulty.HARD), DT1, DT2, GOLD_PRESSURE_PLATE, IRON_PRESSURE_PLATE,
    COMP1, COMP2, COMP3, COMP4, COMPARATOR_BLOCK, COMP5, COMP6, COMP7, COMP8, COMP9, COMP10, COMP11, COMP12, COMP13, COMP14, COMP15, COMP16, COMP17, COMP18, COMP19, COMP20, COMP21, COMP22, COMP23,
    DAYLIGHT_SENSOR_INVERTED, DAYLIGHT_SENSOR(Difficulty.HARD), DAYLIGHT_BOTTOM, DAYLIGHT_SIDE, REDSTONE_BLOCK(Difficulty.MEDIUM), NETHER_QUARTZ_ORE, HOPPER(Difficulty.MEDIUM), HOPPER_SIDE1, HOPPER_SIDE2, HOPPER_SIDE3, HOPPER_TOP,
    QUARTZ_BLOCK(Difficulty.HARD), QUARTZ1, QUARTZ2, QUARTZ3, QUARTZ4, QUARTZ5, QUARTZ6, QUARTZ7, QUARTZ_STAIRS(Difficulty.HARD), QS1, QS2, QS3, AR1, ACTIVATOR_RAIL(Difficulty.HARD), AR2, AR3,
    DROPPER(Difficulty.HARD), DROPPER_TOP, DROPPER_SIDE, DROPPER_BOTTOM, WHITE_TERRACOTTA(Difficulty.MEDIUM), ORANGE_TERRACOTTA(Difficulty.MEDIUM), MAGENTA_TERRACOTTA(Difficulty.MEDIUM), LIGHT_BLUE_TERRACOTTA(Difficulty.MEDIUM),
    YELLOW_TERRACOTTA(Difficulty.MEDIUM), LIME_TERRACOTTA(Difficulty.MEDIUM), PINK_TERRACOTTA(Difficulty.EXPERT), GRAY_TERRACOTTA(Difficulty.MEDIUM), LIGHT_GRAY_TERRACOTTA(Difficulty.MEDIUM), CYAN_TERRACOTTA(Difficulty.MEDIUM),
    PURPLE_TERRACOTTA(Difficulty.MEDIUM), BLUE_TERRACOTTA(Difficulty.MEDIUM), BROWN_TERRACOTTA(Difficulty.MEDIUM), GREEN_TERRACOTTA(Difficulty.MEDIUM), RED_TERRACOTTA(Difficulty.MEDIUM), BLACK_TERRACOTTA(Difficulty.MEDIUM),
    WHITE_STAINED_GLASS_PANE(Difficulty.MEDIUM), ORANGE_STAINED_GLASS_PANE(Difficulty.MEDIUM), MAGENTA_STAINED_GLASS_PANE(Difficulty.MEDIUM), LIGHT_BLUE_STAINED_GLASS_PANE(Difficulty.MEDIUM),
    YELLOW_STAINED_GLASS_PANE(Difficulty.MEDIUM), LIME_STAINED_GLASS_PANE(Difficulty.MEDIUM), PINK_STAINED_GLASS_PANE(Difficulty.EXPERT), GRAY_STAINED_GLASS_PANE(Difficulty.MEDIUM), LIGHT_GRAY_STAINED_GLASS_PANE(Difficulty.MEDIUM),
    CYAN_STAINED_GLASS_PANE(Difficulty.MEDIUM), PURPLE_STAINED_GLASS_PANE(Difficulty.MEDIUM), BLUE_STAINED_GLASS_PANE(Difficulty.MEDIUM), BROWN_STAINED_GLASS_PANE(Difficulty.MEDIUM), GREEN_STAINED_GLASS_PANE(Difficulty.MEDIUM),
    RED_STAINED_GLASS_PANE(Difficulty.MEDIUM), BLACK_STAINED_GLASS_PANE(Difficulty.MEDIUM), ACACIA_STAIRS, AS1, AS2, AS3, DARK_OAK_STAIRS, DOS1, DOS2, DOS3, SLIME_BLOCK, BARRIER,
    IRON_TRAPDOOR(Difficulty.MEDIUM), IS, PRISMARINE, PR1, PR2, PR3, PR4, PR5, PR6, PR7, PR8, PR9, PR10, PR11, PR12, PR13, PR14, PR15, PR16, PR17, PR18, PR19, PR20, PR21,
    HAY_BALE_TOP, HAY_BALE(Difficulty.HARD), HAY_BALE_SIDE, CAR1, CAR2, CAR3, CAR4, CAR5, CAR6, CAR7, CAR8, CAR9, CAR10, CAR11, CAR12, CAR13, CAR14, CAR15, CAR16, TERRACOTTA(Difficulty.MEDIUM), COAL_BLOCK(Difficulty.MEDIUM),
    PACKED_ICE, SUNFLOWER(Difficulty.HARD), SF1, SF2, SF3, SF4, SF5, SF6, SF7, SF8, SF9, SF10, SF11, SF12, SF13, SF14,
    b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21, b22, b23, b24, b25, b26, b27, b28, b29, b30, b31, b32,
    RED_SANDSTONE(Difficulty.HARD), RS1, RS2, RS3, RS4, RED_SANDSTONE_STAIRS(Difficulty.HARD), RSS1, RSS2, RSS3, SL1, SL2, SL3, SL4, SL5, SL6, SL7, SL8, SL9, SL10, SL11, SL12, SL13, SL14, SL15, SL16, SL17, SL18, SL19, SL20, SL21, SL22,
    SL23, SL24, SL25, SL26, SL27, SL28, SL29, SL30, SL31, SL32, SL33, SL34, SPRUCE_FENCE_GATE, BIRCH_FENCE_GATE(Difficulty.EASY), JUNGLE_FENCE_GATE, ACACIA_FENCE_GATE, DARK_OAK_FENCE_GATE,
    SPRUCE_FENCE, BIRCH_FENCE(Difficulty.EASY), JUNGLE_FENCE, ACACIA_FENCE, DARK_OAK_FENCE, DO1, DO2, DO3, DO4, DO5, DO6, DO7, DO8, DO9, DO10, EROD, EROD1, EROD2, EROD3, END_ROD, EROD4,
    CHORUS, CHORUS_FRUIT, CHORUS_PART, PURPUR_BLOCK, PP1, PP2, PP3, PP4, PP5, PP6, PP7, PP8, BR1, BR2, BR3, BR4, GRASS_PATH, GRASS_PATH_TOP, VOID, CMD11, CMD12, CMD13, CMD14, CMD15, CMD16, CMD17, CMD18, CMD19, CMD20, CMD21, CMD22, CMD23, CMD24, CMD25, CMD26, CMD27, CMD28, CMD29, CMD30,
    FROST_WALKER_ICE, FWI, FWI1, FWI2, MAGMA_BLOCK(Difficulty.MEDIUM), NETHER_WART_BLOCK(Difficulty.MEDIUM), IDKWHATTHISIS, BONE_BLOCK(Difficulty.EXPERT), BB1, BB2, STRUCTURE_VOID, OBSERVER(Difficulty.HARD), OBS1, OBS2, OBS3, OBS4, OBS5, SH1, SH2, SH3, SH4, SH5, SH6, SH7, SH8, SH9, SH10,
    SH11, SH12, SH13, SH14, SH15, SH16, SH17, SH18, SH19, SH20, SH21, SH22, SH23, SH24, SH25, SH26, SH27, SH28, SH29, SH30, SH31, SH32, SH33, SH34, SH35, SH36, SH37, SH38, SH39, SH40, SH41, SH42, SH43, SH44, SH45, SH46, SH47, SH48, SH49, SH50, SH51, SH52, SH53, SH54, SH55,
    SH56, SH57, SH58, SH59, SH60, SH61, SH62, SH63, SH64, SH65, SH66, SH67, SH68, SH69, SH70, SH71, SH72, SH73, SH74, SH75, SH76, SH77, SH78, SH79, SH80, SH81, SH82, SH83, SH84, SH85, SH86, SH87, SH88, SH89, SH90, SH91, SH92, SH93, SH94, SH95, SH96, SH97, SH98, SH99, SH100, SH101, SH102,
    GL1, GL2, GL3, GL4, GL5, GL6, GL7, GL8, GL9, GL10, GL11, GL12, GL13, GL14, GL15, GL16, GL17, GL18, GL19, GL20, GL21, GL22, GL23, GL24, GL25, GL26, GL27, GL28, GL29, GL30, GL31, GL32, GL33, GL34, GL35, GL36, GL37, GL38, GL39, GL40, GL41, GL42, GL43, GL44, GL45, GL46, GL47, GL48, GL49, GL50,
    GL51, GL52, GL53, GL54, GL55, GL56, GL57, GL58, GL59, GL60, GL61, GL62, GL63, GL64, WHITE_CONCRETE(Difficulty.HARD), ORANGE_CONCRETE(Difficulty.HARD), MAGENTA_CONCRETE(Difficulty.HARD), LIGHT_BLUE_CONCRETE(Difficulty.HARD), YELLOW_CONCRETE(Difficulty.HARD), LIME_CONCRETE(Difficulty.HARD), PINK_CONCRETE(Difficulty.EXPERT), GRAY_CONCRETE(Difficulty.HARD),
    LIGHT_GRAY_CONCRETE(Difficulty.HARD), CYAN_CONCRETE(Difficulty.HARD), PURPLE_CONCRETE(Difficulty.MEDIUM), BLUE_CONCRETE(Difficulty.HARD), BROWN_CONCRETE(Difficulty.HARD), GREEN_CONCRETE(Difficulty.HARD), RED_CONCRETE(Difficulty.HARD), BLACK_CONCRETE(Difficulty.HARD), WHITE_CONCRETE_POWDER(Difficulty.MEDIUM),
    ORANGE_CONCRETE_POWDER(Difficulty.HARD), MAGENTA_CONCRETE_POWDER(Difficulty.HARD), LIGHT_BLUE_CONCRETE_POWDER(Difficulty.HARD), YELLOW_CONCRETE_POWDER(Difficulty.HARD), LIME_CONCRETE_POWDER(Difficulty.HARD), PINK_CONCRETE_POWDER(Difficulty.EXPERT), GRAY_CONCRETE_POWDER(Difficulty.HARD), LIGHT_GRAY_CONCRETE_POWDER(Difficulty.HARD),
    CYAN_CONCRETE_POWDER(Difficulty.HARD), PURPLE_CONCRETE_POWDER(Difficulty.HARD), BLUE_CONCRETE_POWDER(Difficulty.HARD), BROWN_CONCRETE_POWDER(Difficulty.HARD), GREEN_CONCRETE_POWDER(Difficulty.HARD), RED_CONCRETE_POWDER(Difficulty.HARD), BLACK_CONCRETE_POWDER(Difficulty.HARD), KELP_HEAD, KELP_BODY,
    DRIED_KELP_SIDE, DRIED_KELP_TOP, DRIED_KELP_BLOCK, TE1, TE2, TE3, TE4, TE5, TE6, TE7, TE8, TE9, TE10, TE11, TE12,
    COR1, COR2, COR3, COR4, COR5, COR6, COR7, COR8, COR9, COR10, COR11, COR12, COR13, COR14, COR15, COR16, COR17, COR18, COR19, COR20, COR21, COR22, COR23, COR24, COR25, COR26, COR27, COR28, COR29, COR30, COR31, COR32, COR33, COR34, COR35, COR36, COR37, COR38, COR39, COR40, COR41, COR42, STRUCTURE,
    STRUCTURE1, STRUCTURE2, STRUCTURE3, STRUCTURE4, STRUCTURE5, STRUCTURE6, WTF1, WTF2, WTF3, WTF4, WTF5, WTF6, WTF7, WTF8, WTF9, WTF10, SEA_PICKLE(Difficulty.HARD), HOPPER_ITEM, TURTLE_EGG, IRON_DOOR(Difficulty.MEDIUM), OAK_DOOR(Difficulty.FREE), SPRUCE_DOOR, BIRCH_DOOR(Difficulty.EASY), JUNGLE_DOOR, ACACIA_DOOR,
    DARK_OAK_DOOR, REPEATER(Difficulty.MEDIUM), COMPARATOR(Difficulty.MEDIUM), TURTLE_HELMET(Difficulty.EXPERT), SCUTE(Difficulty.HARD), IRON_SHOVEL(Difficulty.MEDIUM), IRON_PICKAXE(Difficulty.MEDIUM), IRON_AXE(Difficulty.MEDIUM), FLINT_AND_STEEL(Difficulty.EASY), APPLE(Difficulty.EASY), BOW(Difficulty.MEDIUM), BOW_CHARGED, BOW_CHARGED1, BOW_CHARGED2,
    ARROW(Difficulty.EASY), COAL(Difficulty.FREE), CHARCOAL(Difficulty.FREE), DIAMOND(Difficulty.MEDIUM), IRON_INGOT(Difficulty.EASY), GOLD_INGOT(Difficulty.MEDIUM), IRON_SWORD(Difficulty.MEDIUM), WOODEN_SWORD(Difficulty.FREE), WOODEN_SHOVEL(Difficulty.FREE), WOODEN_PICKAXE(Difficulty.FREE), WOODEN_AXE(Difficulty.FREE), STONE_SWORD(Difficulty.FREE), STONE_SHOVEL(Difficulty.FREE),
    STONE_PICKAXE(Difficulty.FREE), STONE_AXE(Difficulty.FREE), DIAMOND_SWORD(Difficulty.HARD), DIAMOND_SHOVEL(Difficulty.HARD), DIAMOND_PICKAXE(Difficulty.HARD), DIAMOND_AXE(Difficulty.HARD), STICK(Difficulty.FREE), BOWL(Difficulty.FREE), MUSHROOM_SOUP(Difficulty.EASY), GOLD_SWORD(Difficulty.EASY), GOLD_SHOVEL(Difficulty.EASY), GOLD_PICKAXE(Difficulty.MEDIUM), GOLD_AXE(Difficulty.EASY),
    STRING(Difficulty.FREE), FEATHER(Difficulty.FREE), GUNPOWDER(Difficulty.EASY), WOODEN_HOE(Difficulty.FREE), STONE_HOE(Difficulty.FREE), IRON_HOE(Difficulty.EASY), DIAMOND_HOE(Difficulty.MEDIUM), GOLD_HOE(Difficulty.MEDIUM), WHEAT_SEED(Difficulty.FREE), WHEAT(Difficulty.MEDIUM), BREAD(Difficulty.MEDIUM), LEATHER_HELMET(Difficulty.MEDIUM), LEATHER_CHESTPLATE(Difficulty.MEDIUM),
    LEATHER_LEGGINGS(Difficulty.MEDIUM), LEATHER_SHOES(Difficulty.MEDIUM), CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_SHOES, IRON_HELMET(Difficulty.EASY), IRON_CHESTPLATE(Difficulty.MEDIUM), IRON_LEGGINGS(Difficulty.MEDIUM), IRON_SHOES(Difficulty.EASY), DIAMOND_HELMET(Difficulty.EXPERT), DIAMOND_CHESTPLATE(Difficulty.EXPERT), DIAMOND_LEGGINGS(Difficulty.EXPERT),
    DIAMOND_SHOES(Difficulty.EXPERT), GOLD_HELMET(Difficulty.MEDIUM), GOLD_CHESTPLATE(Difficulty.MEDIUM), GOLD_LEGGINGS(Difficulty.MEDIUM), GOLD_SHOES(Difficulty.MEDIUM), FLINT(Difficulty.FREE), RAW_PORKCHOP(Difficulty.EASY), COOKED_PORKCHOP(Difficulty.EASY), PAINTING(Difficulty.HARD), GOLDEN_APPLE(Difficulty.MEDIUM), ENCHANTED_GOLDEN_APPLE, OAK_SIGN(Difficulty.EASY), BUCKET(Difficulty.EASY),
    WATER_BUCKET(Difficulty.EASY), LAVA_BUCKET(Difficulty.EASY), MINECART(Difficulty.MEDIUM), SADDLE, REDSTONE_DUST(Difficulty.EASY), SNOWBALL(Difficulty.HARD), OAK_BOAT(Difficulty.FREE), LEATHER(Difficulty.EASY), MILK_BUCKET(Difficulty.EASY), FB1, FB2, FB3, FB4, BRICK(Difficulty.EASY), CLAY(Difficulty.FREE), SUGAR_CANE(Difficulty.FREE), KELP(Difficulty.EASY), PAPER(Difficulty.EASY),
    BOOK(Difficulty.EASY), SLIME_BALL, CHEST_MINECART(Difficulty.MEDIUM), FURNACE_MINECART(Difficulty.MEDIUM), EGG(Difficulty.EASY), c1, c2, c3, c4, c5, c6, c7, COMPASS(Difficulty.MEDIUM), c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, FISHING_ROD(Difficulty.EASY), FISHING_ROD_NO_FISH, CL1, CL2, CL3, CL4, CL5, CL6, CL7, CL8,
    CL9, CL10, CL11, CL12, CL13, CL14, CL15, CL16, CL17, CL18, CL19, CL20, CL21, CL22, CL23, CL24, CL25, CL26, CL27, CL28, CL29, CL30, CL31, CL32, CL33, CL34, CL35, CL36, CL37, CL38, CL39, CL40, CL41, CL42, CL43, CL44, CL45, CL46, CL47, CL48, CL49, CL50, CL51, CL52, CL53, CL54, CL55, CL56, CL57, CL58, CL59, CL60, CL61, CL62, CL63, CL64, GLOWSTONE_DUST(Difficulty.MEDIUM),
    RAW_COD(Difficulty.EASY), SALMON(Difficulty.EASY), CLOWNFISH(Difficulty.MEDIUM), PUFFERFISH(Difficulty.MEDIUM), COOKED_COD(Difficulty.EASY), COOKED_SALMON(Difficulty.EASY), INK_SACK(Difficulty.EASY), RED_DYE(Difficulty.EASY), GREEN_DYE(Difficulty.EASY), COCOA_BEANS(Difficulty.HARD), LAPIS_LAZULI(Difficulty.MEDIUM), PURPLE_DYE(Difficulty.MEDIUM), CYAN_DYE(Difficulty.MEDIUM), LIGHT_GRAY_DYE(Difficulty.MEDIUM),
    GRAY_DYE(Difficulty.MEDIUM), PINK_DYE(Difficulty.EXPERT), LIME_DYE(Difficulty.MEDIUM), YELLOW_DYE(Difficulty.MEDIUM), LIGHT_BLUE_DYE(Difficulty.MEDIUM), MAGENTA_DYE(Difficulty.MEDIUM), ORANGE_DYE(Difficulty.MEDIUM), BONE_MEAL(Difficulty.EASY), BONE(Difficulty.EASY), SUGAR(Difficulty.EASY), CAKE(Difficulty.HARD), WHITE_BED(Difficulty.EASY), ORANGE_BED(Difficulty.MEDIUM), MAGENTA_BED(Difficulty.MEDIUM),
    LIGHT_BLUE_BED(Difficulty.MEDIUM), YELLOW_BED(Difficulty.MEDIUM), LIME_BED(Difficulty.MEDIUM), PINK_BED(Difficulty.EXPERT), GRAY_BED(Difficulty.MEDIUM), LIGHT_GRAY_BED(Difficulty.MEDIUM), CYAN_BED(Difficulty.MEDIUM), PURPLE_BED(Difficulty.MEDIUM), BLUE_BED(Difficulty.MEDIUM), BROWN_BED(Difficulty.MEDIUM), GREEN_BED(Difficulty.MEDIUM), RED_BED(Difficulty.MEDIUM), BLACK_BED(Difficulty.MEDIUM),
    COOKIE, MAP, MAP2, MAP3, SHEARS(Difficulty.EASY), MELON(Difficulty.HARD), DRIED_KELP(Difficulty.EASY), MELON_SEEDS(Difficulty.HARD), PUMPKIN_SEEDS(Difficulty.HARD), RAW_BEEF(Difficulty.EASY), COOKED_BEEF(Difficulty.EASY), RAW_CHICKEN(Difficulty.EASY), COOKED_CHICKEN(Difficulty.EASY), ROTTEN_FLESH(Difficulty.EASY), ENDERPEARL(Difficulty.HARD), BLAZE_ROD, GHAST_TEAR, GOLD_NUGGET(Difficulty.MEDIUM);

    private static final class Helper {
        static int id = 0;
    }

    @Getter
    int startX, startY, endX, endY;
    boolean obtainable = false;
    Difficulty difficulty = Difficulty.EASY;

    MaterialTexture() {
        int x = Helper.id % 16;
        int y = (int) Math.floor(Helper.id / 16);
        Helper.id++;

        startX = x * 16;
        startY = y * 16;

        endX = startX + 16;
        endY = startY + 16;
    }

    enum Difficulty {
        FREE, EASY, MEDIUM, HARD, EXPERT
    }

    MaterialTexture(Difficulty difficulty) {
        this();
        obtainable = true;
        this.difficulty = difficulty;
    }

    public static Map<MaterialTexture, Material> random(int free, int easy, int medium, int hard, int extreme) {
        int currentF = 0, currentEa = 0, currentM = 0, currentH = 0, currentEx = 0;
        HashMap<MaterialTexture, Material> objectHashMap = new HashMap<>(free + easy + medium + hard + extreme);

        boolean done = false;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (!done) {
            MaterialTexture randomTexture = MaterialTexture.values()[random.nextInt(MaterialTexture.values().length)];
            if (randomTexture.obtainable) {
                try {
                    Material mat = Material.valueOf(randomTexture.name().toUpperCase());
                    if (!objectHashMap.containsKey(randomTexture))
                        switch (randomTexture.difficulty) {
                            case FREE:
                                if (!(currentF >= free)) {
                                    currentF++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case EASY:
                                if (!(currentEa >= easy)) {
                                    currentEa++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case MEDIUM:
                                if (!(currentM >= medium)) {
                                    currentM++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case HARD:
                                if (!(currentH >= hard)) {
                                    currentH++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case EXPERT:
                                if (!(currentEx >= extreme)) {
                                    currentEx++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                        }
                } catch (IllegalArgumentException ignored) {
                    // Ignored.
                }
            }

            if (currentF >= free && currentEa >= easy && currentM >= medium && currentH >= hard && currentEx >= extreme)
                done = true;
        }

        return objectHashMap;
    }

}
