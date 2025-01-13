// represent a spreadsheet cell that contains data as a string( text , num , formula)

public class SCell implements Cell {
    private String line; // data
    private int type; // cell type
    private int order ; // compute order


    public SCell(String s) {
        setData(s);
    }
    @Override
    public int getOrder() {
        return order;
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        line = s;
        determineType();
    }

    @Override
    public String getData()
    {
        return line;
    }

    @Override
    public int getType()
    {
        return type;
    }
    @Override
    public void setType(int t)
    {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        order = t;
    }
    // help function to set the type based on the data
    private void determineType() {
        if (line == null || line.isEmpty()) {
            type = Ex2Utils.TEXT;
        } else if (line.startsWith("=")) {
            type = Ex2Utils.FORM;
        } else {
            try {
                Double.parseDouble(line);
                type = Ex2Utils.NUMBER;
            } catch (NumberFormatException e) {
                type = Ex2Utils.TEXT;
            }
        }
    }
}
