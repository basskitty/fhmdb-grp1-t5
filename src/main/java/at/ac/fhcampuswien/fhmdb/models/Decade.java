package at.ac.fhcampuswien.fhmdb.models;

public enum Decade {
    TWENTY_TWENTIES("2020s", 2020, 2029),
    TWENTY_TENS("2010s", 2010, 2019),
    TWO_THOUSANDS("2000s", 2000, 2009),
    NINETIES("1990s", 1990, 1999),
    EIGHTIES("1980s", 1980, 1989),
    SEVENTIES("1970s", 1970, 1979),
    SIXTIES("1960s", 1960, 1969),
    FIFTIES("1950s", 1950, 1959),
    FOURTIES("1940s", 1940, 1949);


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
