package model;

import javafx.animation.AnimationTimer;
import model.AvlNode;

public class AnimatorAvlNode {

    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private AvlNode circle;
    private AnimationTimer timer;
    private long previousTime;
    private double delay;
    private double timeX;
    private double timeY;
    private int fx,fy;
    
    
    public AnimatorAvlNode(double startX, double startY,double endX, double endY,
            AvlNode circle,double delayS){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.circle = circle;
        this.delay = delayS;
        
        if(startX<endX)fx=1;
        else fx = -1;
        
        if(startY<endY) fy=1;
        else fy =-1;
        
        timeX = (endX-startX)/delay/60;
        timeY = (endY-startY)/delay/60;
        
        circle.setLayoutX(startX);
        circle.setLayoutY(startY);
        lula();
    }
    
    private void transition(){
        if(fx*circle.getLayoutX()-fx*endX>0 && fy*circle.getLayoutY()-fy*endY>0)
            timer.stop();
        if(fx*circle.getLayoutX()-fx*endX<0)
            circle.setLayoutX(circle.getLayoutX()+timeX);
        if(fy*circle.getLayoutY()-fy*endY<0)
            circle.setLayoutY(circle.getLayoutY()+timeY);
    }
    
    private void lula(){
       timer = new AnimationTimer() {
           @Override
           public void handle(long now) {
               if(now-previousTime>500000){
                   previousTime= now;
                       transition();
               }
           }
       };
    }
    
    public void start(){
        timer.start();
    }
    
    public void stop(){
        timer.stop();
    }
    
}
