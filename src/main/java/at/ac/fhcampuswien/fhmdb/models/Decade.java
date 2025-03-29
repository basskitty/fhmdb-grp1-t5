package at.ac.fhcampuswien.fhmdb.models;

public enum Decade {
    TWENTIES("1920-1929", 1920, 1929),
    THIRTIES("1930-1939", 1930, 1939),
    FOURTIES("1940-1949", 1940, 1949),
    FIFTIES("1950-1959", 1950, 1959),
    SIXTIES("1960-1969", 1960, 1969),
    SEVENTIES("1970-1979", 1970, 1979),
    EIGHTIES("1980–1989", 1980, 1989),
    NINETIES("1990–1999", 1990, 1999),
    TWO_THOUSANDS("2000–2009", 2000, 2009),
    TWENTY_TENS("2010–2019", 2010, 2019),
    TWENTY_TWENTIES("2020–2029", 2020, 2029);

    private final String label;
    private final int startYear;
    private final int endYear;

    Decade(String label, int startYear, int endYear) {
        this.label = label;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public String getLabel() {
        return label;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    @Override
    public String toString() {
        return label;
    }

}
