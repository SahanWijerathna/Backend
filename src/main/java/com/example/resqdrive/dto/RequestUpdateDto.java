package com.example.resqdrive.dto;

public class RequestUpdateDto {

    private String status;
    private AdditionalFieldsDto additionalFields;

    public RequestUpdateDto() {}

    public static class AdditionalFieldsDto {
        private String garageId;
        private String garageName;
        private String towingId;
        private String towingName;
        private String eta;
        private TechnicianDto technician;

        public AdditionalFieldsDto() {}

        public static class TechnicianDto {
            private String id;
            private String name;
            private String phone;
            public TechnicianDto() {}
            public String getId() { return id; }
            public void setId(String id) { this.id = id; }
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getPhone() { return phone; }
            public void setPhone(String phone) { this.phone = phone; }
        }

        // Getters and Setters
        public String getGarageId() { return garageId; }
        public void setGarageId(String garageId) { this.garageId = garageId; }

        public String getGarageName() { return garageName; }
        public void setGarageName(String garageName) { this.garageName = garageName; }

        public String getTowingId() { return towingId; }
        public void setTowingId(String towingId) { this.towingId = towingId; }

        public String getTowingName() { return towingName; }
        public void setTowingName(String towingName) { this.towingName = towingName; }

        public String getEta() { return eta; }
        public void setEta(String eta) { this.eta = eta; }

        public TechnicianDto getTechnician() { return technician; }
        public void setTechnician(TechnicianDto technician) { this.technician = technician; }
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public AdditionalFieldsDto getAdditionalFields() { return additionalFields; }
    public void setAdditionalFields(AdditionalFieldsDto additionalFields) { this.additionalFields = additionalFields; }
}
