package jpcompany.smartwire.domain;

import java.time.LocalDateTime;

public class Member {
    private long id;

    private String loginId;
    private String loginPassword;
    private String companyName;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public Member() {}
    public Member(String loginId, String loginPassword, String companyName) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.companyName = companyName;
    }

    public Member(long id, String loginId, String loginPassword, String companyName) {
        this.id = id;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.companyName = companyName;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDate) {
        this.createdDateTime = createdDate;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDate) {
        this.updatedDateTime = updatedDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", companyName='" + companyName + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", updatedDateTime=" + updatedDateTime +
                '}';
    }
}
