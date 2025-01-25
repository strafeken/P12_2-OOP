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
        EntityType typeA = a.getEntityType();
        EntityType typeB = b.getEntityType();

        if ((typeA == EntityType.BUCKET && typeB == EntityType.DROP) || (typeA == EntityType.DROP && typeB == EntityType.BUCKET)) 
            return CollisionType.BUCKET_DROP;
        
        if ((typeA == EntityType.CIRCLE && typeB == EntityType.DROP) || (typeA == EntityType.DROP && typeB == EntityType.CIRCLE))
            return CollisionType.CIRCLE_DROP;

        return null;
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
