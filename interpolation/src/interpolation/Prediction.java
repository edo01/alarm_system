/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpolation;

/**
 *
 * @author Edoardo Carrà
 */
public class Prediction {
    public final short averageOfDerivative1 = 1;
    public final short averageOfDerivative1and2 = 2;
    public final short derivative1 = 3;
    public final short derivative1and2 = 4;
    
    private short key; 
    
    private int starting_number;
    private int L_limit, G_limit, range;
    private double prediction;
    private double error = 0;
    
    public Prediction(short key, int from, int lower_limit , int greater_limit){
        switch(key){
            case averageOfDerivative1:
                this.key = averageOfDerivative1;
                break;
            case averageOfDerivative1and2:
                this.key = averageOfDerivative1and2;
                break;
            case derivative1:
                this.key = derivative1;
                break;
            case derivative1and2:
                this.key =  derivative1and2;
                break;
            default:
                this.key = 1;
        }          
        if(greater_limit-lower_limit<0){
            int temp = greater_limit;
            greater_limit = lower_limit;
            lower_limit = temp;
        }
        this.G_limit = greater_limit;
        this.L_limit = lower_limit;
        range = G_limit-L_limit;
        this.starting_number = from;
    }
       
    //la previsione è sul numero finale del prossimo range preso in considerazione
    public double calcPrediction(Interpolation function){
        Polinomial_Function derivata1 = function.calc_derivative(1);
        Polinomial_Function derivata2 = function.calc_derivative(2);
        switch(key){
            case averageOfDerivative1:
                prediction = derivata1.averageValueInRange(L_limit, G_limit)*
                        (2*G_limit-starting_number-L_limit) + 
                        function.valueIn(L_limit+starting_number);
                break;
            case averageOfDerivative1and2:
                prediction = 0.5*derivata2.averageValueInRange(L_limit, G_limit)*
                        (G_limit+starting_number-L_limit)*(G_limit+
                        starting_number-L_limit)+
                        derivata1.averageValueInRange(L_limit, G_limit)*
                        (G_limit+starting_number-L_limit) + 
                        function.valueIn(L_limit+starting_number);
            case derivative1:
                prediction = function.valueIn(L_limit+starting_number) +
                        derivata1.valueIn(
                            function.valueIn(L_limit+starting_number))*
                            (G_limit-L_limit+starting_number);
            case derivative1and2:
                prediction = function.valueIn(L_limit+starting_number) + 
                    derivata1.valueIn(function.valueIn(L_limit+starting_number))
                    *(range+starting_number)+0.5*derivata2.valueIn
                    (function.valueIn(L_limit+starting_number))*
                    (range+starting_number)*(range+starting_number);
        }       
        return prediction;
    }
    
    public void updateError(Interpolation function){
        error += Math.abs(prediction-
                    ((Point)(function.getPoints().lastElement())).getY());
    }

    public double getPrediction() {
        return prediction;
    }

    public double getError() {
        return error;
    }

    @Override
    public String toString() {
        return "Prediction{" + "key=" + key + ", starting_number=" + starting_number + ", prediction=" + prediction + ", error=" + error + '}';
    }
    
    
    
}
