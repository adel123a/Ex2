//the CellEntry class represents a 2D cell index in a spreadsheet format
public class CellEntry  implements Index2D {

    private String cellIndex;// the cell index as a string
    public CellEntry(String index) {
        this.cellIndex = index;
    }

    @Override
    // check if index in valid format
    public boolean isValid() {
        if (cellIndex.isEmpty()|| cellIndex == null) {
            return false;
        }
        // regex to validate format - letter and number
        return cellIndex.matches("^[A-Za-z][0-9]{1,2}$");

    }

    @Override
    // convert letter part to num
    public int getX() {
        if (!isValid()) {
            return Ex2Utils.ERR;
        }
        char letter = Character.toUpperCase(cellIndex.charAt(0));//for easy calc
        return letter - 'A'; // A=0 ,C=3 ...
    }

    @Override
    public int getY() {
        if (!isValid()) {
            return Ex2Utils.ERR;
        }
        try {
            return Integer.parseInt(cellIndex.substring(1)); // the num part
        } catch (NumberFormatException e) {
            return Ex2Utils.ERR;
        }
    }
    public String toString() {
        return cellIndex; // return the original string
    }
}