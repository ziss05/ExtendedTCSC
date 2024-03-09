package beans;

import com.github.kiprobinson.bigfraction.BigFraction;

import java.util.ArrayList;

public class CostOrigin {
    private BigFraction cost;
    private ArrayList<Quadruple> quadruples;

    public CostOrigin() {
        quadruples=new ArrayList<>();
    }

    public CostOrigin(CostOrigin point){
        this.cost=point.getCost();
        this.quadruples =point.getQuadruples();
    }

    public BigFraction getCost() {
        if (cost==null){ return null;}
        return cost;
    }

    public void setCost(BigFraction cost) {
        if (cost==null){ this.cost=null;}
        this.cost = cost;
    }

    public ArrayList<Quadruple> getQuadruples() {
        return quadruples;
    }

    public void setQuadruples(ArrayList<Quadruple> q) {
        this.quadruples = q;
    }

    public boolean addQuadruple(Quadruple q) {
        return this.quadruples==null?false:this.quadruples.add(q);
    }

    public void addQuadruples(ArrayList<Quadruple> quadruples) {
        for (Quadruple q:quadruples) {
            this.quadruples.add(q);
        }
    }

}
