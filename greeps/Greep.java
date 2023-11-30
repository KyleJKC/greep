import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
import java.util.List;

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * @Kyle Jin
 * @Nov 28, 2023
 */
public class Greep extends Creature
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    /**
     * Default constructor for testing purposes.
     */
    public Greep()
    {
        this(null);
    }

    
    /**
     * Create a Greep with its home space ship.
     */
    public Greep(Ship ship)
    {
        super(ship);
    }
    

    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        if(!carryingTomato())
        {
            searchForTomatoes();
        }
        else
        {
            returnToShip();
        }
    }

    /**
    * Only got 30 seconds to find tomatoes
    */
    private void searchForTomatoes()
    {
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        if(tomatoes == null){
            moveFollowPaint();
        } else{
            if(isTouching(Greep.class)){
                loadTomato();
            } else{
                meetWithCompanion();
            }
        }
    }
    
    /**
    * Get back to the ship to unload then look for more tomatoes
    */
    private void returnToShip()
    {
        if(atShip()) 
        {
            dropTomato();
        }
        else 
        {
            turnHome();
            if(atWater()||atWorldEdge()){
                turn(120);
            }
            move();
            spit("red");
        }
    }
    
    /**
     * Is there any food here where we are? If so, try to load some!
     */
    private void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        if(tomatoes != null) 
        {
            loadTomato();

            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
        }
    }

    private void meetWithCompanion(){
        
        if(getMemory() == 4){
            move();
            setMemory(0);
        } else {
            setMemory(getMemory() + 1);
        }
        System.out.println("executed0");
        
        Greep closestGreep = null;
        List<Greep> greepList = getObjectsInRange(20, Greep.class);
        double closestDistance = Double.MAX_VALUE;
        for(Greep greep : greepList){
            double distance = getDistance(greep);
            if(distance < closestDistance){
                closestDistance = distance;
                closestGreep = greep;
                System.out.println("executed1");
            }
        }
        if(closestGreep!=null){
            turnTowards(closestGreep.getX(), closestGreep.getY());
            move();
            System.out.println("executed2");
        }
    }
    
    private void moveFollowPaint(){
        List<Paint> paintList = getObjectsInRange(20, Paint.class);
        Paint closestPaint = null;
        double closestDistance = Double.MAX_VALUE;
        if(paintList != null && closestPaint != null){
            move();
            if(atWater()||atWorldEdge()){
                turn(60);
            }
        } else  {
        for(Paint paint : paintList){
            double distance = getDistance(paint);
            if(distance < closestDistance){
                closestDistance = distance;
                closestPaint = paint;
                System.out.println("executed1");
            }
        }
        if(closestPaint!=null){
            turnTowards(closestPaint.getX(), closestPaint.getY());
            move();
            System.out.println("executed2");
        }
        }
    }
    
    private double getDistance(Actor actor) {
        int dx = this.getX() - actor.getX();
        int dy = this.getY() - actor.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    /**
     * This method specifies the name of the author (for display on the result board).
     */
    public static String getAuthorName()
    {
        return "Kyle Jin";  // write your name here!
    }


    /**
     * This method specifies the image we want displayed at any time. (No need 
     * to change this for the competition.)
     */
    public String getCurrentImage()
    {
        if(carryingTomato())
            return "greep-with-food.png";
        else
            return "greep.png";
    }

}