package io.github.team2;

public class CollisionResolver {
	
	private EntityManager em;
	private PointsManager pm;
	
	public CollisionResolver(EntityManager entityManager, PointsManager pointsManager)
	{
		em = entityManager;
		pm = pointsManager;
	}
	
    public void resolveCollision(Entity a, Entity b)
    {
    	CollisionType collisionType = CollisionType.getCollisionType(a, b);
    	
    	if (collisionType == null)
    		return;	
    	
    	switch (collisionType)
    	{
    		case PLAYER_DROP:
    			handlePlayerDropCollision(a, b);
    			break;
    		case PLAYER_BUCKET:
    			handlePlayerBucketCollision(a, b);
    			break;
    		case BUCKET_DROP:
    			handleBucketDropCollision(a, b);
    			break;
    		case CIRCLE_DROP:
    			handleCircleDropCollision(a, b);
    			break;
    		default:
    			System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
    	}
    }
    
    private void handlePlayerDropCollision(Entity a, Entity b)
    {
        System.out.println("handle collision: PLAYER | DROP");
    }
    
    private void handlePlayerBucketCollision(Entity a, Entity b)
    {
        System.out.println("handle collision: PLAYER | BUCKET");
    }
    
    private void handleBucketDropCollision(Entity a, Entity b)
    {
        System.out.println("handle collision: BUCKET | DROP");
        
        if (a.getEntityType() == EntityType.DROP)
        	em.markForRemoval(a);
        else
        	em.markForRemoval(b);
        
        pm.addPoints(10);
    }

    private void handleCircleDropCollision(Entity a, Entity b)
    {
        System.out.println("handle collision: CIRCLE | TRIANGLE");
    }
}
