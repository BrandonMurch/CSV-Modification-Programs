package OrganizeTags;

import java.util.ArrayList;
import java.util.List;

public class TagsInCsvLine {
    private String zeroSO2;
    private String petNat;
    private String danger;
    private String glou;
    private String oxidative;
    private final List<String> others = new ArrayList<>();

    public String toCSVFormat() {
        String csvLine = zeroSO2 + ", " + petNat + ", " + danger  + ", " + glou + ", " + oxidative + ", " + others;
        return csvLine.replaceAll("[\\[\\]]", "").replaceAll("null", "");
    }

    public void addOther(String tag) {
        others.add(tag);
    }

    public void setZeroSO2(String zeroSO2) {
        this.zeroSO2 = zeroSO2;
    }

    public void setPetNat(String petNat) {
        this.petNat = petNat;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public void setGlou(String glou) {
        this.glou = glou;
    }

    public void setOxidative(String oxidative) {
        this.oxidative = oxidative;
    }
}
