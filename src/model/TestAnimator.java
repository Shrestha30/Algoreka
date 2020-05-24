package model;

import javafx.animation.AnimationTimer;
import model.AvlNode;

public class TestAnimator {

    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private TestNode circle;
    private AnimationTimer timer;
    private long previousTime;
    private double delay;
    private double timeX;
    private double timeY;
    private int fx,fy;
    
    
    public TestAnimator(double startX, double startY,double endX, double endY,
            TestNode circle,double delay){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.circle = circle;
        this.delay = delay;
        
        if(startX<endX)fx=1;
        else fx = -1;
        
        if(startY<endY) fy=1;
        else fy =-1;
        
        timeX = (endX-startX)/delay/60;
        timeY = (endY-startY)/delay/60;
        
        circle.getCircle().setCenterX(startX);
        circle.getCircle().setCenterY(startY);
        lula();
    }
    
    private void transition(){
        if(fx*circle.getCircle().getCenterX()-fx*endX>0 && fy*circle.getCircle().getCenterY()-fy*endY>0)
            timer.stop();
        if(fx*circle.getCircle().getCenterX()-fx*endX<0)
            circle.getCircle().setCenterX(circle.getCircle().getCenterX()+timeX);
        if(fy*circle.getCircle().getCenterY()-fy*endY<0)
            circle.getCircle().setCenterY(circle.getCircle().getCenterY()+timeY);
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
