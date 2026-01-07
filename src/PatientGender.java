public enum PatientGender {
    M, F;

    public static PatientGender fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Gender is required.");
        }

        String v = value.trim().toUpperCase();

        if (v.equals("M")) return M;
        if (v.equals("F")) return F;

        throw new IllegalArgumentException("Gender must be 'M' or 'F'.");
    }
}
