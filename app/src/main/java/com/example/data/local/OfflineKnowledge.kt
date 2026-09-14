package com.example.data.local

import com.example.data.model.EducationTopic
import com.example.data.model.EnvironmentalTip
import com.example.data.model.RecyclingGuideItem

object OfflineKnowledge {

    val recyclingGuides = listOf(
        RecyclingGuideItem(
            id = "guide_plastic_bottle",
            name = "Plastic Bottles & Containers (PET / HDPE)",
            category = "Plastic",
            material = "Polyethylene Terephthalate (#1) & High-Density Polyethylene (#2)",
            description = "Everyday beverage bottles, cooking oil bottles, detergent jugs, and shampoo containers.",
            howToRecycle = listOf(
                "Rinse empty containers with rainwater to prevent odours and insects.",
                "Crush bottles flat to save transportation and storage space.",
                "Separate caps and rings if collection centers require it.",
                "Take clean batches to formal collection depots where available (e.g. Honiara recycling initiatives)."
            ),
            reuseTips = listOf(
                "Cut lower section to create nursery pots for tomato, cabbage, or chili seedlings.",
                "Create an inverted drip irrigation feeder buried next to plant roots.",
                "Keep seeds, matches, fishing hooks, or nails dry from humid marine air.",
                "Build vertical hanging garden walls along veranda rails."
            ),
            safetyWarning = "NEVER burn plastics in yard bonfires. Burning releases toxic dioxins, leads to respiratory infections, and contaminates nearby vegetable patches.",
            solomonContext = "In island communities without rubbish collection, plastic accumulation in lagoons threatens sea turtles, coral reefs, and coastal fisheries. Prioritize domestic reuse before holding for transport to town."
        ),
        RecyclingGuideItem(
            id = "guide_glass_bottles",
            name = "Glass Bottles & Jars",
            category = "Glass",
            material = "Soda-lime glass",
            description = "Beverage bottles, jam jars, coffee jars, and medicine containers.",
            howToRecycle = listOf(
                "Rinse with warm water and dry thoroughly.",
                "Check for bottle deposit return programs run by local beverage distributors in provincial centers.",
                "Separate lids (often metal or plastic) for proper sorting."
            ),
            reuseTips = listOf(
                "Airtight food storage for dried beans, coconut chips, rice, or sea salt to protect from ants and humidity.",
                "Water planters and propagation jars for ornamental plants and fresh herbs.",
                "Decorative garden path borders by inverting matching bottles neck-down in sand."
            ),
            safetyWarning = "Wrap chipped or broken glass in sturdy canvas or multiple layers of cardboard before handling to prevent severe cuts.",
            solomonContext = "Glass is inert and does not release toxic microplastics into reefs, but broken shards on beaches and coral flats cause severe injuries to bare feet."
        ),
        RecyclingGuideItem(
            id = "guide_metal_cans",
            name = "Aluminium Cans & Steel Tins",
            category = "Metal",
            material = "Aluminium & Tin-plated Steel",
            description = "Soft drink cans, canned tuna tins, canned corned beef tins, fruit tins.",
            howToRecycle = listOf(
                "Rinse food residues thoroughly to prevent disease vectors.",
                "Crush aluminium cans with a wood mallet to compress volume.",
                "Sell aluminium and clean scrap metal to local scrap metal buyers in Honiara and provincial ports."
            ),
            reuseTips = listOf(
                "Puncture drainage holes in tuna/fruit cans to grow spring onions and ginger.",
                "Punch artistic hole patterns in clean tins to make wind-proof candle or kerosene lanterns.",
                "Use sturdy tins as pencil holders, workshop nail sorters, or small paint pots."
            ),
            safetyWarning = "Smooth or press down sharp rim edges with pliers before giving tin containers to children or using as planters.",
            solomonContext = "Aluminium holds real cash value at metal recycling exporters in the Solomon Islands. Collecting and bagging aluminium cans provides supplemental household income while cleaning up coastal areas."
        ),
        RecyclingGuideItem(
            id = "guide_cardboard_paper",
            name = "Cardboard Boxes & Paper",
            category = "Cardboard & Paper",
            material = "Corrugated Cardboard, Kraft Paper, Newsprint",
            description = "Shipping cartons from trade stores, food packaging, cartons, office paper.",
            howToRecycle = listOf(
                "Keep cardboard dry and flat; wet cardboard degrades rapidly in tropical humidity.",
                "Bundle flat boxes with rope or twine."
            ),
            reuseTips = listOf(
                "Sheet Mulching: Lay flat plain cardboard over garden beds, wet thoroughly, and top with compost to suffocate invasive weeds naturally.",
                "Construct sturdy under-bed organizers, toy boxes, or classroom storage trays.",
                "Tear plain brown cardboard into strips to provide essential carbon ('browns') for rich compost piles."
            ),
            safetyWarning = "Avoid using heavily glossy or chemically treated colored cardboards in edible vegetable gardens.",
            solomonContext = "Cardboard is biodegradable, making it a valuable soil conditioner in coral sand soils when shredded and composted."
        ),
        RecyclingGuideItem(
            id = "guide_organic_waste",
            name = "Organic Waste, Scraps & Coconut Husks",
            category = "Organic Waste",
            material = "Biodegradable biomass",
            description = "Kitchen vegetable peelings, coconut husks, banana skins, grass clippings, leaves.",
            howToRecycle = listOf(
                "Separate food waste from plastics and metals at the kitchen level.",
                "Build a designated compost bay enclosed with bamboo or tree stakes.",
                "Layer moist food scraps ('greens') with dry leaves, coconut fiber, and sawdust ('browns')."
            ),
            reuseTips = listOf(
                "Coconut husks make excellent natural mulch to retain moisture around root vegetables during dry spells.",
                "Finished compost dramatically boosts yields of kumara, cassava, pumpkin, and leafy greens without expensive chemical fertilizers.",
                "Clean vegetable scraps can supplement backyard chicken feed."
            ),
            safetyWarning = "Do not include meat, fish guts, or animal feces in open compost piles near homes to prevent stray dogs, rats, and flies.",
            solomonContext = "Up to 65% of municipal waste in Solomon Islands towns is organic. Diverting organic waste into home compost eliminates landfill buildup and revitalizes island soil."
        ),
        RecyclingGuideItem(
            id = "guide_tyres",
            name = "Discarded Vehicle & Bicycle Tyres",
            category = "Tyres & Rubber",
            material = "Vulcanized Rubber & Steel Belts",
            description = "Worn car, truck, tractor, and bicycle tyres.",
            howToRecycle = listOf(
                "Store out of rainwater to eliminate mosquito breeding pockets for dengue and malaria.",
                "Coordinate with community public works for civil erosion control."
            ),
            reuseTips = listOf(
                "Terrace steep slopes and riverbank borders by stacking tyres filled with tamped earth.",
                "Create raised garden beds for root crops and herbs.",
                "Build comfortable outdoor stools and swing seats wrapped in natural coir rope."
            ),
            safetyWarning = "NEVER set fire to scrap tyres. Tyre fires release carcinogenic black smoke, sulphur dioxide, and toxic oils that seep into groundwater and marine habitats.",
            solomonContext = "Tyres left in coastal rain collect stagnant water, becoming primary breeding sites for dengue and malaria mosquitoes. Always punch drainage holes if using in gardens."
        ),
        RecyclingGuideItem(
            id = "guide_electronics_batteries",
            name = "Batteries & Electronics (E-Waste)",
            category = "Electronics & Batteries",
            material = "Lead, Lithium, Mercury, Cadmium, Circuit boards",
            description = "Old mobile phones, solar lighting batteries, alkaline batteries, power banks, radios.",
            howToRecycle = listOf(
                "Store old batteries in a dry plastic tub away from moisture and heat.",
                "Do not throw batteries into the sea or pit toilets.",
                "Bring e-waste to registered electronic scrap programs or municipal hazardous collection points."
            ),
            reuseTips = listOf(
                "Salvage working switches, copper wires, and LED diodes for domestic electrical repairs.",
                "Keep working screens or cases for spare phone parts."
            ),
            safetyWarning = "DANGER: Lead-acid solar and vehicle batteries contain corrosive sulfuric acid and poisonous heavy metals. Never crack them open or dump acid on the ground. Handle with thick rubber gloves.",
            solomonContext = "Because solar power is widespread across island villages, dead batteries are a major environmental challenge. Protecting drinking water and reef lagoons from battery acid is vital."
        ),
        RecyclingGuideItem(
            id = "guide_textiles",
            name = "Old Clothing, Canvas & Textiles",
            category = "Textiles",
            material = "Cotton, Polyester, Synthetic canvas",
            description = "Worn t-shirts, torn trousers, bedsheets, canvas sacks.",
            howToRecycle = listOf(
                "Wash and dry clean fabric scraps.",
                "Sort 100% natural fibers from synthetic fabrics."
            ),
            reuseTips = listOf(
                "Cut into reusable household dishcloths, boat cleaning rags, and workshop grease wipes.",
                "Sew patchwork shopping bags to eliminate single-use plastic carrier bags at markets.",
                "Tear into soft fabric ties to support tomato plants and climbing beans without cutting plant stems."
            ),
            safetyWarning = "Synthetic fabrics (polyester, nylon) will melt into burning skin if exposed to fire; keep away from open cooking hearths.",
            solomonContext = "Fabric tote bags provide a long-lasting, washable alternative to plastic bags at Central Market in Honiara, Gizo, and Auki."
        )
    )

    val environmentalTips = listOf(
        EnvironmentalTip(
            id = 1,
            title = "Protect Our Reefs & Lagoons",
            content = "Plastic bottles and snack wrappers washed into rivers end up choking coral reefs and fish in our lagoons. Keep village drains and riverbanks clean.",
            category = "Ocean & Reefs"
        ),
        EnvironmentalTip(
            id = 2,
            title = "Never Burn Plastics",
            content = "Burning plastics in village bonfires releases toxic black smoke that causes asthma and harms children's lungs. Repurpose containers instead.",
            category = "Health & Safety"
        ),
        EnvironmentalTip(
            id = 3,
            title = "Turn Coconut Waste into Soil Food",
            content = "Dry coconut husks, dead banana leaves, and kitchen scraps make nutrient-rich compost for sweet potato and leafy greens without buying chemical fertilizer.",
            category = "Gardening & Soil"
        ),
        EnvironmentalTip(
            id = 4,
            title = "Bring a Bilibil or Basket to Market",
            content = "Say no to single-use thin plastic bags when shopping at provincial markets. A woven basket or cloth bag lasts for years and keeps our beaches clean.",
            category = "Community Action"
        ),
        EnvironmentalTip(
            id = 5,
            title = "Stop Mosquito Breeding in Waste",
            content = "Tyres, open tin cans, and broken bottles trap rainwater and breed malaria and dengue mosquitoes. Turn them upside down or puncture drainage holes.",
            category = "Community Health"
        ),
        EnvironmentalTip(
            id = 6,
            title = "Safe Handling for Solar Batteries",
            content = "Never pour acid from dead solar or vehicle batteries onto coastal sand. The heavy metals poison groundwater wells and nearby shellfish beds.",
            category = "Hazardous Waste"
        ),
        EnvironmentalTip(
            id = 7,
            title = "Repair Before You Replace",
            content = "Mending a fishing net, sewing torn clothing, or replacing a broken wooden handle saves hard-earned cash and keeps waste out of our islands.",
            category = "Resourcefulness"
        )
    )

    val educationTopics = listOf(
        EducationTopic(
            id = "topic_plastic_ocean",
            title = "Why Plastic Pollution Matters to Our Oceans",
            subtitle = "Preserving Solomon Islands marine heritage and fishing livelihoods",
            icon = "water",
            content = "The Solomon Islands are blessed with some of the most biodiverse marine ecosystems on Earth. When plastic bags, bottles, and foam packaging are thrown onto roads or washed down rivers, they float into coastal mangroves, lagoons, and coral reefs. Marine turtles confuse transparent plastic bags with jellyfish and choke. Over time, sunlight and saltwater break plastics into microscopic pieces called microplastics, which fish consume. Protecting our reefs from plastic ensures sustainable fish for future generations.",
            keyTakeaways = listOf(
                "Plastic takes 450+ years to degrade in marine environments.",
                "Fish eat toxic microplastics, which enter the human food chain.",
                "Healthy reefs provide natural barrier protection against cyclones and coastal erosion."
            )
        ),
        EducationTopic(
            id = "topic_burning_hazards",
            title = "The Hidden Dangers of Burning Plastic",
            subtitle = "Why open burning is harmful to family health and village air",
            icon = "warning",
            content = "In many rural communities without waste trucks, burning rubbish has become a common habit. However, plastics (especially PVC, polystyrene foam, and colored plastic bags) release deadly chemical compounds called dioxins, furans, and carbon monoxide when burned at low bonfire temperatures. Breathing this smoke triggers severe asthma attacks, chronic bronchitis, eye irritation, and increases long-term cancer risks. The ashes left behind contain toxic heavy metals that wash into nearby garden vegetables and well water.",
            keyTakeaways = listOf(
                "Never burn plastic bottles, foam containers, or rubber tyres.",
                "Dioxins from burning plastic settle on edible garden greens.",
                "Compost organic waste separately so you only have minimal non-burnable materials."
            )
        ),
        EducationTopic(
            id = "topic_composting_basics",
            title = "Composting Basics for Island Gardens",
            subtitle = "Transforming food scraps and garden leaves into rich black soil",
            icon = "eco",
            content = "Up to 60-70% of waste produced in Solomon households is organic matter: vegetable peelings, coconut meat, banana skins, breadfruit scraps, and fallen leaves. Instead of filling rubbish pits, this material can be turned into rich organic fertilizer. Build a simple 1-meter square pen using bamboo stakes. Layer moist green waste (kitchen scraps) with dry brown waste (coconut husks, dried leaves, cardboard). Keep it moist like a wrung-out sponge, turn it every two weeks with a garden fork, and in 6-8 weeks you will have fertile soil for your garden.",
            keyTakeaways = listOf(
                "Green layers provide nitrogen; brown layers provide carbon and aeration.",
                "Never add raw meat or grease to prevent attracting stray dogs or rodents.",
                "Saves household money by avoiding commercial chemical fertilizers."
            )
        ),
        EducationTopic(
            id = "topic_reduce_reuse_hierarchy",
            title = "The Waste Hierarchy: Practical Island Action",
            subtitle = "Reduce -> Reuse -> Repair -> Repurpose -> Recycle -> Dispose",
            icon = "recycling",
            content = "Because shipping recyclable materials off remote Pacific islands is expensive and complex, we cannot rely solely on factory recycling. The most effective approach is the Island Waste Hierarchy: First, REDUCE unnecessary packaging. Second, REUSE containers and bottles for storage and seeds. Third, REPAIR broken tools, furniture, and nets. Fourth, REPURPOSE items into garden beds or crafts. Fifth, collect clean metals for scrap buyers. Finally, DISPOSE safely of hazardous items away from streams.",
            keyTakeaways = listOf(
                "Reuse and repair come before recycling.",
                "Local ingenuity turns everyday scrap into valuable household assets.",
                "Every container reused is one less item cluttering our coastlines."
            )
        )
    )
}
