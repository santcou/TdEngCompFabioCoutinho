package td2final.engecomp.td2final.Model;

/**
 * Created by Fabio on 25/01/2018.
 */

public class Dispositivo
{
    private String ruaDispositivo;
    private String numeroDispositivo;
    private String telefoneDispositivo;
    private String responsavelDispositivo;
    private String cidadeDispositivo;
    private String bairroDispositivo;
    private String PmResponsavelDispositivo;
    private String NomeComercioDispositivo;

    public Dispositivo() {
    }

    public String getBairroDispositivo() {
        return bairroDispositivo;
    }

    public void setBairroDispositivo(String bairroDispositivo) {
        this.bairroDispositivo = bairroDispositivo;
    }

    public String getNomeComercioDispositivo() {
        return NomeComercioDispositivo;
    }

    public void setNomeComercioDispositivo(String nomeComercioDispositivo) {
        NomeComercioDispositivo = nomeComercioDispositivo;
    }

    public String getCidadeDispositivo() {
        return cidadeDispositivo;
    }

    public void setCidadeDispositivo(String cidadeDispositivo) {
        this.cidadeDispositivo = cidadeDispositivo;
    }

    public String getPmResponsavelDispositivo() {
        return PmResponsavelDispositivo;
    }

    public void setPmResponsavelDispositivo(String pmResponsavelDispositivo) {
        PmResponsavelDispositivo = pmResponsavelDispositivo;
    }

    public String getRuaDispositivo() {
        return ruaDispositivo;
    }

    public void setRuaDispositivo(String ruaDispositivo) {
        this.ruaDispositivo = ruaDispositivo;
    }

    public String getNumeroDispositivo() {
        return numeroDispositivo;
    }

    public void setNumeroDispositivo(String numeroDispositivo) {
        this.numeroDispositivo = numeroDispositivo;
    }

    public String getTelefoneDispositivo() {
        return telefoneDispositivo;
    }

    public void setTelefoneDispositivo(String telefoneDispositivo) {
        this.telefoneDispositivo = telefoneDispositivo;
    }

    public String getResponsavelDispositivo() {
        return responsavelDispositivo;
    }

    public void setResponsavelDispositivo(String responsavelDispositivo) {
        this.responsavelDispositivo = responsavelDispositivo;
    }
}
