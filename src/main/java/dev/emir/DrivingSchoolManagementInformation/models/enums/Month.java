package dev.emir.DrivingSchoolManagementInformation.models.enums;

public enum Month {
    OCAK("Ocak"),
    ŞUBAT("Şubat"),
    MART("Mart"),
    NİSAN("Nisan"),
    MAYIS("Mayıs"),
    HAZİRAN("Haziran"),
    TEMMUZ("Temmuz"),
    AĞUSTOS("Ağustos"),
    EYLÜL("Eylül"),
    EKİM("Ekim"),
    KASIM("Kasım"),
    ARALIK("Aralık");

    private final String displayName;

    Month(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 