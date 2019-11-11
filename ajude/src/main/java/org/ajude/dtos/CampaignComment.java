package org.ajude.dtos;

public class CampaignComment
{
    private Long idComment;
    private Long idCampaign;

    public CampaignComment(Long idComment, Long idCampaign)
    {
        this.idComment = idComment;
        this.idCampaign = idCampaign;
    }

    public Long getIdComment()
    {
        return idComment;
    }

    public void setIdComment(Long idComment)
    {
        this.idComment = idComment;
    }

    public Long getIdCampaign()
    {
        return idCampaign;
    }

    public void setIdCampaign(Long idCampaign)
    {
        this.idCampaign = idCampaign;
    }
}
