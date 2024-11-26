/**
 * Enum represents different types of tretments in the Clinic
 * Each treatment type has a fixed price
 */
public enum Treatment {
    ACNE_TREATMENT(2750),
    SKIN_WHITENING(7650),
    MOLE_REMOVAL(3850),
    LASER_TREATMENT(12500);

    private final double price;

    Treatment(double price) {
        this.price = price;
    }

    public double getPrice() { return price; }
}
