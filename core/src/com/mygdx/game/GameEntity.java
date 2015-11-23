package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by bryancasperchan on 23/11/2015.
 */
public class GameEntity extends Rectangle{
    private Texture sprite;
    private String  entityClass;
    private float spriteScale = 1.0f;
    private int screenBoundWidth;
    private int screenBoundHeight;
    private float moveSpeed;

    public GameEntity(){

    }

    public GameEntity(Texture spr, String cls, float scale, float screenWidth, float screenHeight, float move){
        this.entityClass = cls;
        this.sprite = spr;
        this.spriteScale = scale;
        this.width = (int) (this.sprite.getWidth() * this.spriteScale);
        this.height = (int) (this.sprite.getWidth() * this.spriteScale);
        this.screenBoundHeight = (int) screenHeight;
        this.screenBoundWidth = (int) screenWidth;
        this.x = this.screenBoundHeight/2 - width/2;
        this.y = this.screenBoundHeight/2 - height/2;
        this.moveSpeed = move;
    }

    public void setClass(String s){
        entityClass = s;
    }

    public void computePosAndSize(){
        this.width = (int) (this.sprite.getWidth() * this.spriteScale);
        this.height = (int) (this.sprite.getWidth() * this.spriteScale);
        this.x = this.screenBoundHeight/2 - this.width/2;
        this.y = this.screenBoundHeight/2 - this.height/2;
    }
    public Texture getSprite(){
        return sprite;
    }
    public void setSprite(Texture t){
        sprite = t;
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
        if(x < 0) x = 0;
        if(x > screenBoundWidth - width) x = screenBoundWidth - width;
        if(y < 0) y = 0;
        if(y > screenBoundHeight - height) y = screenBoundHeight - height;
    }


    public void randomMove(float delta) {
        x += MathUtils.random(-1.0f, 1.0f) * moveSpeed * screenBoundWidth * delta;
        y += MathUtils.random(-1.0f,1.0f) * moveSpeed * screenBoundHeight * delta;
        enforceBounds();
    }

    public void moveToPos(int targetX, int targetY , float delta) {
        float deltaX = (targetX - (x + width/2))*delta;
        float deltaY = ((screenBoundHeight - targetY) - (y + height/2)) * delta;
        x += deltaX;
        y += deltaY;
        enforceBounds();
    }

    public void setPosTo(int targetX, int targetY){
        x = targetX - (width/2);
        y = (screenBoundHeight - targetY - (height/2));
        enforceBounds();
    }
}
