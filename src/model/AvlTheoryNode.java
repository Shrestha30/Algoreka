package model;


public class AvlTheoryNode  
{   
    private int key;  
    private AvlTheoryNode left;  
    private AvlTheoryNode right;  
    private int height;
    private AvlNode node;
    
    public AvlTheoryNode(int key){
        this.key = key;
        left=null;
        right=null;
        height=1;
    }
    
    public int getKey(){
        return key;
    }
    
    public void setKey(int key){
        this.key = key;
    }
    
    public AvlTheoryNode getLeft(){
        return left;
    }
    
    public void setLeft(AvlTheoryNode left){
        this.left = left;
    }
    
    public AvlTheoryNode getRight(){
        return right;
    }
    
    public void setRight(AvlTheoryNode right){
        this.right = right;
    }
    
    public void setHeight(int height){
        this.height = height;
    }
    
    public int getHeight(){
        return height;
    }
    
    public AvlNode getNode(){
        return node;
    }
    
    public void setNode(AvlNode node){
        this.node = node;
    }
    
};