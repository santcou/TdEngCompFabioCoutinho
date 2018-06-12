package td2final.engecomp.td2final.Model;

/**
 * Created by Fabio on 22/01/2018.
 */

public class Usuario
{
    private String usuarioNome;
    private String usuarioNomeGuerra;
    private String usuarioEmail;
    private String usuarioSenha;
    private String usuarioNumeroPM;

    public Usuario(String usuarioNome, String usuarioNomeGuerra, String usuarioEmail, String usuarioSenha, String usuarioNumeroPM) {
        this.usuarioNome = usuarioNome;
        this.usuarioNomeGuerra = usuarioNomeGuerra;
        this.usuarioEmail = usuarioEmail;
        this.usuarioSenha = usuarioSenha;
        this.usuarioNumeroPM = usuarioNumeroPM;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioNomeGuerra() {
        return usuarioNomeGuerra;
    }

    public void setUsuarioNomeGuerra(String usuarioNomeGuerra) {
        this.usuarioNomeGuerra = usuarioNomeGuerra;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getUsuarioSenha() {
        return usuarioSenha;
    }

    public void setUsuarioSenha(String usuarioSenha) {
        this.usuarioSenha = usuarioSenha;
    }

    public String getUsuarioNumeroPM() {
        return usuarioNumeroPM;
    }

    public void setUsuarioNumeroPM(String usuarioNumeroPM) {
        this.usuarioNumeroPM = usuarioNumeroPM;
    }
}
