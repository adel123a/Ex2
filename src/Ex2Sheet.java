import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;



public class Ex2Sheet implements Sheet {
    private Cell[][] table;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        Cell c = get(x, y);
        if (c != null) {
            if (c.getType() == Ex2Utils.FORM) {
                ans = eval(x, y);
            } else {
                // text or number returned as is
                ans = c.toString();
            }
        }
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        // if it inside the bounds
        if (isIn(x, y)) {
            return table[x][y];
        }
        return null; // if not in bounds
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        Index2D index = new CellEntry(cords);
        // check if in valid format
        if (index.isValid() && isIn(index.getX(), index.getY())) {
            ans = table[index.getX()][index.getY()];
        }
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        if (isIn(x, y))
            table[x][y] = c;
    }

    @Override
    public void eval() {
        int[][] dd = depth();
    }


    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && xx < width() && yy >= 0 && yy < height();
    }

    @Override
    public int[][] depth() { // to store depth of each cell
        int w = width();
        int h = height();
        int[][] ans = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                boolean[][] procceced = new boolean[w][h]; // for tracking if cell been processed
                ans[i][j] = computeDepth(i, j, procceced); // calc cell depth
                if (ans[i][j] == -1) {
                    table[i][j].setType(Ex2Utils.ERR_CYCLE_FORM); // self dependency
                }
            }
        }
        return ans; //return the depths
    }

    @Override
    public void load(String fileName) throws IOException {
        try (Scanner scanner = new Scanner(new File(fileName))) { //read the file
            if (scanner.hasNextLine()) scanner.nextLine(); // first line is just header
            while (scanner.hasNextLine()) {
                String rowData = scanner.nextLine();
                String[] parts = rowData.split(",", 3); // (x,y) cords and value
                if (parts.length == 3) {
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());
                    String value = parts[2].trim();
                    if (isIn(x, y)) {
                        set(x, y, value);
                    }
                }
            }
        }
    }

    @Override
    public void save(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("* I2CS ArielU: SpreadSheet (Ex2) assignment\n");
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    String data = table[i][j].getData();
                    if (!data.isEmpty()) {
                        writer.write(String.format("%d,%d,%s%n", i, j, data));
                    }
                }
            }
        }
    }

    @Override
    public String eval(int x, int y) {
        Cell c = get(x, y);
        if (c == null)
            return Ex2Utils.EMPTY_CELL;
        int cellType = c.getType();
        if (cellType == Ex2Utils.TEXT || cellType == Ex2Utils.NUMBER) {
            return c.getData();
        }
        // if its a form , calc it
        if (cellType == Ex2Utils.FORM) {
            String writtenForm = c.getData().trim(); // remove whitespace
            if (writtenForm.startsWith("=")) {
                writtenForm = writtenForm.substring(1).trim();
            }
            try {
                String replacedValues = replaceReference(writtenForm);// replace refernce to cell with actual value
                // evaluate when multiple operators
                double numericVal = evalExpression(replacedValues);
                return String.valueOf(numericVal);
            } catch (Exception e) {
                return Ex2Utils.ERR_FORM;
            }
        }
        return c.getData();
    }
    //function for replace refernce to cell,  with cell value
    public String replaceReference(String formula) {
        // remove = if exist
        if (formula.startsWith("=")) {
            formula = formula.substring(1).trim();
        }
        String spaced = formula.replaceAll("([+\\-*/()])", " $1 "); // add space near oprators for easy calc
        String[] parts = spaced.trim().split("\\s+"); // spilt when whitspace
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.matches("[A-Za-z]\\d+")) // check if its in cell sturcture
            {
                String numValue = getReferenceValue(part);
                newString.append(numValue).append(" "); // for easy calc
            } else {
                newString.append(part).append(" ");
            }
        }
        return newString.toString().trim();
    }

    private String getReferenceValue(String reference) {
        char column = Character.toUpperCase(reference.charAt(0));
        int x = column - 'A'; //column index in numbers
        String rowStr = reference.substring(1); // number part (string)
        int y; //num part int
        try {
            y = Integer.parseInt(rowStr);
        } catch (Exception e) {
            return "0";
        }
        if (!isIn(x, y)) {
            return "0";
        }
        String value = eval(x, y);
        if (!isNumber(value)) {         //if not num

            return "0";
        }
        return value;
    }

    //function for string calculation with arithmetic operations
    public double evalExpression(String expration) {
        expration = expration.trim();
        if (isNumber(expration)) { //num only
            return Double.parseDouble(expration);
        }
        // if parentheses exist- evaluate inside
        if (isParentheses(expration)) {
            return evalExpression(expration.substring(1, expration.length() - 1));
        }
        // find the main operator index
        int inedxM = indOfMainOp(expration);
        if (inedxM == -1) {
            throw new IllegalArgumentException("invalid expression");   // no main opreator find

        }
        char op = expration.charAt(inedxM);         // extract operator
        // split left+right
        String left = expration.substring(0, inedxM).trim();
        String right = expration.substring(inedxM + 1).trim();
        // recursively evaluate left + right
        double valLeft = evalExpression(left);
        double valRight = evalExpression(right);
        // combine sides
        switch (op) {
            case '*':
                return valLeft * valRight;
            case '/':
                if (valRight == 0)
                    throw new ArithmeticException("divide by zero");
                return valLeft/ valRight;
            case '+':
                return valLeft + valRight;
            case '-':
                return valLeft - valRight;
            default:
                throw new IllegalArgumentException("unknown operator");
        }
    }
    //check if there is pairs of parentheses:
    public boolean isParentheses(String expr) {
        if (!expr.startsWith("(") || !expr.endsWith(")"))
            return false;
        int count = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') count++;
            else if (c == ')') count--;
            if (count == 0 && i < expr.length() - 1) {
                // not a pair of parnthese
                return false;
            }
        }
        if (count == 0) // same number of open and close parentheses
            return true;
        return false;
    }

     // function for finding the index of the main operator in expression ,-1 if none

    public int indOfMainOp(String expration) {
        int mainDepth = 0;
        int index = -1;
        for (int i = 0; i < expration.length(); i++) { // go through all of the input string
            char c = expration.charAt(i);
            if (c == '(') {
                mainDepth++;
            }
            else if (c == ')') {
                mainDepth--;
            }
            else if (mainDepth == 0) {
                if (c == '*' || c == '-' || c == '+' || c == '/') { // main op - out of the ()
                    // return the first main operator
                    return i;
                }
            }
        }
        return index;
    }
//function for check if the input is a double type num
    public boolean isNumber(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public int computeDepth(int x, int y, boolean[][] processed) {
        // Check for circular dependency
        if (processed[x][y]) {
            return -1; // circular dependency
        }
        // mark cell as processed
        processed[x][y] = true;
        Cell c = get(x, y);
        if (c == null || c.getType() == Ex2Utils.TEXT || c.getType() == Ex2Utils.NUMBER) {   // no dependencies for null -text or number cells
            processed[x][y] = false;
            return 0;
        }
        int maxDepth = 0;
        if (c.getType() == Ex2Utils.FORM) {
            String data = c.getData().trim().replaceFirst("^=", "");// for calc
            String[] parts = data.split("\\s+");
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.matches("[A-Za-z]\\d+")) { // Check if valid cell reference
                    int depX = Character.toUpperCase(part.charAt(0)) - 'A';
                    int depY;
                    try {
                        depY = Integer.parseInt(part.substring(1));
                    } catch (NumberFormatException e) {
                        continue; // skip invalid references
                    }
                    if (isIn(depX, depY)) {
                        int depth = computeDepth(depX, depY, processed);
                        if (depth == -1) {
                            return -1; // circular dependency
                        }
                        maxDepth = Math.max(maxDepth, depth); // return the large num
                    }
                }
            }
        }
        processed[x][y] = false;
        if (maxDepth > 0)
            return 1 + maxDepth;
        else
            return 0;
    }
}
