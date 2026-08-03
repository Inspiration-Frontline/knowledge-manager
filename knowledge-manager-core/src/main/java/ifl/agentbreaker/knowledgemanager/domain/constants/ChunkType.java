package ifl.agentbreaker.knowledgemanager.domain.constants;

public enum ChunkType
{
    DOCUMENT,

    DOCUMENT_IMAGE,

    IMAGE,

    VIDEO,
    ;

    public boolean isUserCreatable()
    {
        return this == DOCUMENT || this == IMAGE || this == VIDEO;
    }
}