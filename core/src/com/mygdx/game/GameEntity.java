package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by bryancasperchan on 23/11/2015.
 */
public class GameEntity extends Sprite{

    public static final int PLAYER = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    private int  entityClass;
    private float spriteScale = 1.0f;
    private int screenBoundWidth;
    private int screenBoundHeight;
    private float moveSpeed;

    public GameEntity(){

    }

    public GameEntity(Texture spr){
        super(spr);
    }

    public GameEntity(Texture spr, int cls, float scale, float screenWidth, float screenHeight, float move){
        super(spr);
        this.entityClass = cls;
        this.spriteScale = scale;

        this.screenBoundHeight = (int) screenHeight;
        this.screenBoundWidth = (int) screenWidth;
        computePosAndSize();
        this.moveSpeed = move;
    }

    public int getEntityClass(){
        return entityClass;
    }
    public void setEntityClass(int cls){
        entityClass = cls;
    }

    public void computePosAndSize(){
        float width = (int) (getWidth() * this.spriteScale);
        float height = (int) (getWidth() * this.spriteScale);
        float x = this.screenBoundHeight/2 - width/2;
        float y = this.screenBoundHeight/2 - height/2;
        setBounds(x,y,width,height);
    }

    public void setSpriteScale(float f){
        spriteScale = f;
    }

    public void setMoveSpeed(float f){
        moveSpeed = f;

    }

    public void setScreenBounds(int x, int y){
        screenBoundWidth = x;
        screenBoundHeight = y;
    }

    public void enforceBounds(){
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        if(x < 0) x = 0;
        if(x > screenBoundWidth - width) x = screenBoundWidth - width;
        if(y < 0) y = 0;
        if(y > screenBoundHeight - height) y = screenBoundHeight - height;
        setX(x);
        setY(y);
    }


    public void randomMove(float delta) {
        float x = getX() + MathUtils.random(-1.0f, 1.0f) * moveSpeed * screenBoundWidth * delta;
        float y = getY() + MathUtils.random(-1.0f,1.0f) * moveSpeed * screenBoundHeight * delta;
        setX(x);
        setY(y);
        enforceBounds();
    }

    public void moveToPos(int targetX, int targetY , float delta) {
        float deltaX = (targetX - (getX() + getWidth()/2))*delta;
        float deltaY = ((screenBoundHeight - targetY) - (getY() + getHeight()/2)) * delta;
        float x = getX() + deltaX;
        float y = getY() + deltaY;
        setX(x);
        setY(y);
        enforceBounds();
    }

    public void setPosTo(int targetX, int targetY){
        float x = targetX - (getWidth()/2);
        float y = (screenBoundHeight - targetY - (getHeight()/2));
        setX(x);
        setY(y);
        enforceBounds();
    }
}
