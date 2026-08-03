package ifl.agentbreaker.knowledgemanager.domain.constants;

public enum ChunkType
{
    DOCUMENT(true),

    DOCUMENT_IMAGE(false),

    IMAGE(true),

    VIDEO(true),
    ;

    private final boolean userManaged;

    ChunkType(boolean userManaged)
    {
        this.userManaged = userManaged;
    }

    public boolean isUserManaged()
    {
        return userManaged;
    }
}