package at.ac.fhcampuswien.fhmdb.models;

public enum Sorting
{
    NAME_ASC("Sort - Name (asc)"),
    NAME_DESC("Sort - Name (desc)"),
    CAT_ASC("Sort - Category (asc)"),
    CAT_DESC("Sort - Category (desc)");

    public final String buttonText;

    Sorting(String buttonText)
    {
        this.buttonText = buttonText;
    }
}
