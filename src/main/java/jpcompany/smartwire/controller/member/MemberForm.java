package jpcompany.smartwire.controller.member;

public class MemberForm {
    private String companyName;
    private String loginId;
    private String loginPassword;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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


    @Override
    public String toString() {
        return "MemberForm{" +
                "companyName='" + companyName + '\'' +
                ", loginId='" + loginId + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                '}';
    }
}
