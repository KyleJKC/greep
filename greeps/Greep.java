import greenfoot.*; // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * @Kyle Jin
 * @Last Edit: 12/3/23
 */
public class Greep extends Creature {
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!

    /**
     * Default constructor for testing purposes.
     */
    public Greep() {
        this(null);
    }

    /**
     * Create a Greep with its home space ship.
     */
    public Greep(Ship ship) {
        super(ship);
    }

    // Start changes here

    /*
     * Do what a greep's gotta do.
     */
    public void act() {
        super.act(); // do not delete! leave as first statement in act().
        // Coming out for the first time
        if (atShip()) {
            if (getMemory() == 0) {
                setMemory(1);
            }

            setFlag(1, false);
            int rand = Greenfoot.getRandomNumber(2);

            if (rand % 2 == 0) {
                setFlag(2, true);
            } else {
                setFlag(2, false);
            }
        }

        if (carryingTomato()) {
            if (atShip()) {
                dropTomato();
                turn(180);
                setMemory(2);
                setFlag(1, false);
                setFlag(2, true);
            } else {
                // need to do Deliver
                deliverToShip();
            }
            move(); // done once while having tomatoes
        } else { // not found tomatoes yet
            if (!atFood()) {
                // need to do Search
                searchForFood();
                move();
            } else { // at food pile
                // wait
                waitForGreep();
            }

        }
    }

    public void searchForFood() {
        int count = getMemory();

        if (count == 1) {
            // first time...
            int x = getX();
            int wx = 800;

            if (x < wx / 2) {
                setRotation(66);
            } else {
                setRotation(233);
            }
            count = 5;
        } else if (atWorldEdge()) {
            toggleBounced();
            changeDirection(isBackwards());
            setMemory(5);
        } else if (atWater()) {
            changeDirection(isBackwards());
            setMemory(5);
        } else if (count < 35) {
            // Do nothing, but keep counting
        } else if (seePaint("purple")) {
            followPaint();
            // Change direction every 100 counts.
        } else if (count % 100 == 0) {
            changeDirection(isBackwards());
        } else if (count % 10 == 0) {
            // Do nothing every ten counts
        } else if (count > 50) {
            if (atShip()) {
                // prevent greep from getting stuck at ship.
                changeDirection(isBackwards());
            }

        }

        // prevent searching greeps from going above 55
        if (count >= 55) {
            count = 5;
            turnAwayShip();
        }

        loopCounter(count);
    }

    public void waitForGreep() {
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        int count = getMemory();
        if (count <= 156) {
            count = 240;
        } else if (count < 248) {
            move();
        } else {
            int dX = tomatoes.getX() - getX();
            int dY = tomatoes.getY() - getY();
            // get into the center of the pile
            setRotation((int) (180 * Math.atan2(dY, dX) / Math.PI));
            move();
        }

        if (!atFood()) {
            searchForFood();
            move();
        }
        if (count >= 254)
            count = 248;

        checkFood();
        loopCounter(count);
    }

    public void deliverToShip() {
        int count = getMemory();

        if (count <= 55 || count >= 239) {
            turnHome();
            count = 60;
        } else if (atWorldEdge()) {
            toggleBounced();
            turnHome();
            count = 60;
        } else if (atWater()) {
            changeDirection(isBackwards());
            count = 60;
        } else if (count <= 64) {
            // do nothing.
            // prevents paint from spitting too close to the pile
        } else if (count % 10 == 0) {
            // do something special every 10 turns
            turnHome();

        } else if (count > 80) {
            spit("purple");
        } else {
            // Empty Block
        }

        if (count < 56 || count >= 254)
            count = 60;

        loopCounter(count);
    }

    /**
     * follows the paint trail
     */
    public void followPaint() {
        if (seePaint("purple")) {
            turnAwayShip();
        }
    }

    /**
     * sets the memory to one less than previous
     */
    public void loopCounter(int count) {
        if (count == 254) {
            count = 0;
        }
        setMemory(count + 1);
    }

    /**
     * turn around
     */
    public void turnAwayShip() {
        turnHome();
        turn(180);
    }

    public void changeDirection(boolean neg) {
        int chance = Greenfoot.getRandomNumber(7);
        int angle = (atWorldEdge() || atShip()) ? 20 + chance : 33;
        if (neg) {
            setRotation(getRotation() - angle);
        } else {
            setRotation(getRotation() + angle);
        }
    }

    public boolean atFood() {
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        if (tomatoes != null) {
            return true;
        }
        return false;
    }

    public boolean hasBounced() {
        return getFlag(1);
    }

    public boolean isBackwards() {
        return getFlag(2);
    }

    public void toggleBounced() {
        setFlag(1, !hasBounced());
    }

    public void toggleBackwards() {
        setFlag(2, !isBackwards());
    }

    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood() {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        if (tomatoes != null) {
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
        } else {
            // Empty Block
        }
    }

    /**
     * This method specifies the name of the author (for display on the result
     * board).
     */
    public static String getAuthorName() {
        return "Kyle Jin"; // write your name here!
    }

    /**
     * This method specifies the image we want displayed at any time. (No need
     * to change this for the competition.)
     */
    public String getCurrentImage() {
        if (carryingTomato())
            return "greep-with-food.png";
        else
            return "greep.png";
    }
}