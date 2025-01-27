package io.github.team2;

public class CollisionResolver {
	
	private EntityManager em;
	
	public CollisionResolver(EntityManager entityManager)
	{
		em = entityManager;
	}
	
    public void resolveCollision(Entity a, Entity b)
    {
    	CollisionType collisionType = getCollisionType(a, b);
    	
    	if (collisionType != null)
    	{
            switch (collisionType)
            {
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
    }
    
    private CollisionType getCollisionType(Entity a, Entity b)
    {
    	if (isPair(a, b, EntityType.BUCKET, EntityType.DROP))
    		return CollisionType.BUCKET_DROP;
    	
    	if (isPair(a, b, EntityType.CIRCLE, EntityType.DROP))
    		return CollisionType.CIRCLE_DROP;

        return null;
    }
    
    private boolean isPair(Entity a, Entity b, EntityType type1, EntityType type2)
    {
        return (a.getEntityType() == type1 && b.getEntityType() == type2) ||
               (a.getEntityType() == type2 && b.getEntityType() == type1);
    }
    
    private void handleBucketDropCollision(Entity a, Entity b)
    {
        System.out.println("handle collision: BUCKET & DROP");
        
        if (a.getEntityType() == EntityType.DROP)
        	em.markForRemoval(a);
        else
        	em.markForRemoval(b);
    }

    private void handleCircleDropCollision(Entity a, Entity b)
    {
        System.out.println("handle collision: CIRCLE & TRIANGLE");
    }
}
