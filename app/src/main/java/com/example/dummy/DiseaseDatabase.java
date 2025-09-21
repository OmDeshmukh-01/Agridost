package com.example.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiseaseDatabase {
    private List<DiseaseInfo> diseases;
    private Random random;

    public DiseaseDatabase() {
        this.diseases = new ArrayList<>();
        this.random = new Random();
        initializeDiseases();
    }

    private void initializeDiseases() {
        // Tomato diseases
        diseases.add(new DiseaseInfo(
            "Early Blight",
            "Dark spots with concentric rings on leaves, yellowing, and defoliation",
            "Remove affected leaves, improve air circulation, apply copper fungicide, water at soil level, and use resistant varieties."
        ));

        diseases.add(new DiseaseInfo(
            "Late Blight",
            "Water-soaked spots on leaves, white mold on underside, rapid plant death",
            "Remove infected plants immediately, apply fungicide, improve drainage, and avoid overhead watering."
        ));

        diseases.add(new DiseaseInfo(
            "Powdery Mildew",
            "White powdery coating on leaves, stunted growth, yellowing",
            "Improve air circulation, apply sulfur fungicide, water early in the day, and use resistant varieties."
        ));

        // Potato diseases
        diseases.add(new DiseaseInfo(
            "Potato Scab",
            "Rough, corky lesions on potato tubers, brown spots",
            "Maintain soil pH 5.2-5.5, use resistant varieties, rotate crops, and avoid over-fertilization with nitrogen."
        ));

        diseases.add(new DiseaseInfo(
            "Potato Blight",
            "Dark lesions on leaves and stems, rapid wilting, tuber rot",
            "Apply fungicide preventively, improve drainage, remove infected plants, and store tubers in cool, dry place."
        ));

        // Leaf diseases
        diseases.add(new DiseaseInfo(
            "Leaf Spot",
            "Circular brown or black spots on leaves, yellowing around spots",
            "Remove affected leaves, improve air circulation, avoid overhead watering, and apply fungicide if severe."
        ));

        diseases.add(new DiseaseInfo(
            "Rust",
            "Orange or reddish-brown pustules on leaves, yellowing, defoliation",
            "Remove infected leaves, improve air circulation, apply fungicide, and use resistant varieties."
        ));

        // Root diseases
        diseases.add(new DiseaseInfo(
            "Root Rot",
            "Wilting, yellowing leaves, stunted growth, dark mushy roots",
            "Improve drainage, avoid overwatering, use well-draining soil, and apply fungicide to soil."
        ));

        diseases.add(new DiseaseInfo(
            "Fusarium Wilt",
            "Yellowing and wilting of leaves, brown vascular tissue, plant death",
            "Remove infected plants, improve soil drainage, use resistant varieties, and practice crop rotation."
        ));

        // Nutrient deficiencies
        diseases.add(new DiseaseInfo(
            "Nitrogen Deficiency",
            "Yellowing of older leaves, stunted growth, poor fruit development",
            "Apply nitrogen fertilizer, use compost, plant nitrogen-fixing crops, and test soil regularly."
        ));

        diseases.add(new DiseaseInfo(
            "Phosphorus Deficiency",
            "Dark green leaves with purple tint, stunted growth, poor root development",
            "Apply phosphorus fertilizer, use bone meal, maintain proper soil pH, and improve soil organic matter."
        ));

        diseases.add(new DiseaseInfo(
            "Potassium Deficiency",
            "Yellowing leaf edges, brown spots, weak stems, poor fruit quality",
            "Apply potassium fertilizer, use wood ash, maintain proper soil pH, and ensure adequate watering."
        ));

        // Pest damage
        diseases.add(new DiseaseInfo(
            "Aphid Damage",
            "Curled leaves, sticky honeydew, stunted growth, sooty mold",
            "Spray with insecticidal soap, introduce beneficial insects, use neem oil, and remove heavily infested plants."
        ));

        diseases.add(new DiseaseInfo(
            "Caterpillar Damage",
            "Holes in leaves, defoliation, frass (droppings) visible",
            "Hand-pick caterpillars, use Bacillus thuringiensis (Bt), apply neem oil, and encourage beneficial insects."
        ));

        diseases.add(new DiseaseInfo(
            "Spider Mite Damage",
            "Fine webbing, yellow stippling on leaves, leaf drop",
            "Spray with water, use insecticidal soap, apply neem oil, and increase humidity around plants."
        ));

        // General plant health
        diseases.add(new DiseaseInfo(
            "Overwatering",
            "Yellowing leaves, wilting, root rot, stunted growth",
            "Reduce watering frequency, improve drainage, check soil moisture before watering, and use well-draining soil."
        ));

        diseases.add(new DiseaseInfo(
            "Underwatering",
            "Wilting, dry soil, brown leaf edges, stunted growth",
            "Increase watering frequency, mulch around plants, check soil moisture regularly, and water deeply."
        ));

        diseases.add(new DiseaseInfo(
            "Sunburn",
            "White or brown patches on leaves, crispy texture, leaf drop",
            "Provide shade during hottest hours, gradually acclimate plants to sun, and ensure adequate watering."
        ));
    }

    public DiseaseInfo getRandomDisease() {
        if (diseases.isEmpty()) {
            return new DiseaseInfo("Unknown", "Unable to identify", "Please consult with a local agricultural expert.");
        }
        return diseases.get(random.nextInt(diseases.size()));
    }

    public DiseaseInfo getDiseaseByName(String name) {
        for (DiseaseInfo disease : diseases) {
            if (disease.getName().toLowerCase().contains(name.toLowerCase())) {
                return disease;
            }
        }
        return getRandomDisease();
    }

    public List<DiseaseInfo> getAllDiseases() {
        return new ArrayList<>(diseases);
    }

    public static class DiseaseInfo {
        private String name;
        private String symptoms;
        private String solution;

        public DiseaseInfo(String name, String symptoms, String solution) {
            this.name = name;
            this.symptoms = symptoms;
            this.solution = solution;
        }

        public String getName() {
            return name;
        }

        public String getSymptoms() {
            return symptoms;
        }

        public String getSolution() {
            return solution;
        }
    }
}
