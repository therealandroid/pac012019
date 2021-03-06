package br.ufg.inf.fabrica.pac.dominio;

import br.ufg.inf.fabrica.pac.dominio.utils.UtilsValidacao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author danilloguimaraes
 */
@Entity
public class Pacote implements Validavel, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String descricao;
    @Temporal(TemporalType.DATE)
    private Date dataCriacao;
    private boolean abandonado;
    private String documento;
    @Temporal(TemporalType.DATE)
    private Date dataPrevistaRealizacao;

    @ManyToOne
    private Estado estado;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private Projeto projeto;
    @OneToMany
    private List<Andamento> andamentos;

    public Pacote() {
        andamentos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAbandonado() {
        return abandonado;
    }

    public void setAbandonado(boolean abandonado) {
        this.abandonado = abandonado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getDataPrevistaRealizacao() {
        return dataPrevistaRealizacao;
    }

    public void setDataPrevistaRealizacao(Date dataPrevistaRealizacao) {
        this.dataPrevistaRealizacao = dataPrevistaRealizacao;
    }

    public List<Andamento> getAndamentos() {
        return andamentos;
    }

    public void setAndamentos(List<Andamento> andamentos) {
        this.andamentos = andamentos;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public List<String> validar() {
        List<String> inconsistencias = new ArrayList<>();
        if(UtilsValidacao.isNullOrEmpty(nome))
            inconsistencias.add("Nome não informado");
        if(UtilsValidacao.isNullOrEmpty(descricao))
            inconsistencias.add("Descrição não informada");
        if(dataCriacao==null)
            inconsistencias.add("Data de criação não informada");
        if(UtilsValidacao.isNullOrEmpty(documento))
            inconsistencias.add("Documento não informado");
        if(estado==null)
            inconsistencias.add("Estado não informado");
        if(projeto==null)
            inconsistencias.add("Projeto não informado");
        if(dataCriacao!=null && dataPrevistaRealizacao!=null && 
                dataCriacao.after(dataPrevistaRealizacao)){
            inconsistencias.add("Data de criação maior que previsão de "
                    + "realização do pacote");
        }
        return inconsistencias;
    }

}
