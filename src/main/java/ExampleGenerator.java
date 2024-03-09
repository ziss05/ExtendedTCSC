import generated.Type;

import java.io.*;

public class ExampleGenerator {
    private int samples;
    private int nodes;
    private int tcMapSize;
    String schemaLocation;
    String outputPath;
    private static final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n";
    private static final Type[] TYPES = Type.values();
    private static int id =0;
    private Integer[][] statistics;

    private int seq = 0;
    private int and = 0;
    private int syncand = 0;
    private int xorc = 0;
    private int xord = 0;
    private int height = 0;
    private int width = 0;

    private void reset(){
        this.nodes++;
        this.seq = 0;
        this.and = 0;
        this.syncand = 0;
        this.xorc = 0;
        this.xord = 0;
        this.height = 0;
        this.width = 0;
    }
    public int getSeq() {
        return seq;
    }

    public int getAnd() {
        return and;
    }

    public int getSyncAnd() {
        return syncand;
    }

    public int getXorc() {
        return xorc;
    }

    public int getXord() {
        return xord;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void printInfo(String name) {
        System.out.println(name + ": " +
                "\nSEQ: " + getSeq() +
                "\nAND: " + getAnd() +
                "\nSYNCAND: " + getSyncAnd() +
                "\nXORC: " + getXorc() +
                "\nXORD: " + getXord() +
                "\nHeight: " + getHeight() +
                "\nWidth: " + getWidth() + "\n"
        );
    }

    public ExampleGenerator(int samples, int nodes, int tcMapSize, String schemaLocation, String outputPath) {
        if (samples <= 0 || nodes <= 0 || tcMapSize <= 0) {
            throw new IllegalArgumentException("Samples, Nodes and TCMapSize need to be bigger than 0!");
        }
        if (nodes % 2 == 0) {
            throw new IllegalArgumentException("Number of nodes must be odd!");
        }
        this.schemaLocation = schemaLocation;
        this.samples = samples;
        this.nodes = nodes;
        this.tcMapSize = tcMapSize;
        this.outputPath = outputPath;
        this.statistics=new Integer[samples][7];
    }

    public Integer[][] getStatistics() {
        return statistics;
    }

    public void generate() {
        StringBuilder fileName = null;
        PrintWriter writer = null;
        for (int i = 0; i < this.samples; i++) {
            try {
                fileName = new StringBuilder(outputPath);
                writer = new PrintWriter(new FileWriter(fileName.append("/").append(String.valueOf(i + 1)).append(".xml").toString()));
                writer.print(HEAD);
                writer.printf("<tree xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "      xsi:noNamespaceSchemaLocation=\"%s\">\n", schemaLocation);

                if (nodes == 1) {
                    writer.println("<node xsi:type=\"basicService\" identifier=\"A\">");
                    this.nodes--;
                } else {
                    Type selected= selectType();
                    writer.printf("<node xsi:type=\"constructor\" type=\"%s\">\n", selected.name());
                    this.nodes--;

                    //int rand = (int)(Math.random() * range) + min;
                    //range=max-min+1
                    int min = (int) (Math.random() * 11) + this.nodes;
                    int max=(int) (Math.random() * 11) + this.tcMapSize*min*10;

                    generateTree(writer, this.nodes, this.height, selected, min, max);
                }

                writer.write("</node>\n</tree>");

            } catch (IOException e) {
                System.out.println("Generator: File could not be written to.");
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }

            printInfo(String.valueOf(i + 1));

            statistics[i][0]=getSeq();
            statistics[i][1]=getAnd();
            statistics[i][2]=getSyncAnd();
            statistics[i][3]=getXorc();
            statistics[i][4]=getXord();
            statistics[i][5]=getHeight();
            statistics[i][6]=getWidth();

            reset();
        }
    }

    private void generateTree(PrintWriter writer, int nodes, int depth, Type rootConstructor, int min, int max) {
        int[][] divided;
        if (nodes - 2 == 0) {
            divided=divideRange(rootConstructor, min, max);
            id++;
            writer.print("<left xsi:type=\"basicService\" identifier=\"");
            writer.print(id);
            writer.println("\">");
            generateTCMap(writer, divided[0][0], divided[0][1]);
            writer.println("</left>");
            id++;
            writer.print("<right xsi:type=\"basicService\" identifier=\"");
            writer.print(id);
            writer.println("\">");
            generateTCMap(writer, divided[1][0], divided[1][1]);
            writer.println("</right>");

            if (this.height <= depth++) {
                this.width = this.height + depth + 1;
                height = depth;
            }
        } else {
            int rand = (int) (Math.random() * (nodes - 1) + 1);
            if (rand % 2 == 0) {
                rand++;
            }
            int left = rand;
            int right = nodes - rand;
            depth++;

            divided=divideRange(rootConstructor, min, max, left, right);
            Type selected;
            if (left == 1) {
                id++;
                writer.print("<left xsi:type=\"basicService\" identifier=\"");
                writer.print(id);
                writer.println("\">");
                generateTCMap(writer, divided[0][0], divided[0][1]);
                writer.println("</left>");
            } else {
                selected= selectType();
                writer.printf("<left xsi:type=\"constructor\" type=\"%s\">\n", selected.name());
                left--;
                generateTree(writer, left, depth,selected,divided[0][0], divided[0][1]);
                writer.println("</left>");
            }
            if (right == 1) {
                id++;
                writer.print("<right xsi:type=\"basicService\" identifier=\"");
                writer.print(id);
                writer.println("\">");
                generateTCMap(writer, divided[1][0], divided[1][1]);
                writer.println("</right>");
            } else {
                selected= selectType();
                writer.printf("<right xsi:type=\"constructor\" type=\"%s\">\n", selected.name());
                right--;
                generateTree(writer, right, depth, selected, divided[1][0], divided[1][1]);
                writer.println("</right>");
            }
        }
    }

    private Type selectType() {
        Type type = TYPES[(int) (Math.random() * 5)];
        switch (type) {
            case SEQ:
                this.seq++;
                break;
            case AND:
                this.and++;
                break;
            case SYNCAND:
                this.syncand++;
                break;
            case XORC:
                this.xorc++;
                break;
            case XORD:
                this.xord++;
                break;
        }
        return type;
    }

    public int[][] divideRange(Type constructor, int min, int max) {
        int[][] range = new int[2][2];
        switch (constructor) {
            case SEQ:
                // :2
                if(min%2==1){
                    min++;
                }
                if (min==1){min=2;}
                range[0][0]=min/2;
                range[0][1]=max/2;
                range[1][0]=min/2;
                range[1][1]=max/2;
                break;
            case AND: //after AND max=max*2!!!
                range[0][0]=min;
                range[0][1]=max/2;
                range[1][0]=min;
                range[1][1]=max/2;
                break;
            case SYNCAND:
            case XORC:
            case XORD:
                //equal range
                range[0][0]=min;
                range[0][1]=max;
                range[1][0]=min;
                range[1][1]=max;
                break;
        }
        return range;
    }

    public int[][] divideRange(Type constructor, int min, int max, int left, int right) {
        int[][] range = new int[2][2];
        switch (constructor) {
            case SEQ:
                if(min%2==1){
                    min++;
                }
                int x=left+right;
                range[0][0]=min/x*left;
                range[0][1]=max/x*left;
                range[1][0]=min/x*right;
                range[1][1]=max/x*right;
                break;
            case AND:
            case SYNCAND:
            case XORC:
            case XORD:
                range=divideRange(constructor, min, max);
                break;
        }
        return range;
    }

    public void generateTCMap(PrintWriter writer, int min, int max) {
        int d1 = min;
        int c1 = 1;
        int d2 = min+1;
        int c2 = 1;

        int upper=(max-min)/this.tcMapSize;

        //random(): 0.0<=x<1.0
        //int rand = (int)(Math.random() * range) + min;
        //range=max-min+1 --> upper
        for (int i = 1; i <= this.tcMapSize; i++) {
            //no directly consecutive constant time intervals
            d1 = i==1?d1:d2;
            c1 = (int) (Math.random() * max + min);
            d2 = i==this.tcMapSize?max:(int) (Math.random() * upper)+ (d1+1);
            c2 = (int) (Math.random() * max + min);

            writer.println("<quadruple>");
            writer.printf("<d1>%d</d1>\n", d1);
            writer.printf("<c1>%d</c1>\n", c1);
            writer.printf("<d2>%d</d2>\n", d2);
            writer.printf("<c2>%d</c2>\n", c2);
            writer.println("</quadruple>");
        }
    }
}
