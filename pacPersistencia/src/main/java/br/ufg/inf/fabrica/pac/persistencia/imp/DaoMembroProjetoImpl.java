package br.ufg.inf.fabrica.pac.persistencia.imp;

import br.ufg.inf.fabrica.pac.negocio.dominio.MembroProjeto;
import br.ufg.inf.fabrica.pac.negocio.dominio.Resposta;
import br.ufg.inf.fabrica.pac.negocio.dominio.Usuario;
import br.ufg.inf.fabrica.pac.persistencia.IDaoMembroProjeto;
import br.ufg.inf.fabrica.pac.persistencia.util.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DaoMembroProjetoImpl implements IDaoMembroProjeto {

    public Resposta<List<Usuario>> buscarUsuarios() {
        String sql = "select U.*, M.* from USUARIO U left join MEMBRO_PROJETO M on U.ID = M.IDUSUARIO order by U.ID";
        Resposta resposta = new Resposta();
        try {
            PreparedStatement pst;
            pst = Conexao.getConnection().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            List<Usuario> usuarios = new ArrayList();
            while(rs.next()){
                Usuario usuario = Util.populaObjeto(Usuario.class, rs);
                MembroProjeto membro = Util.populaObjeto(MembroProjeto.class, rs);
                if(membro.getIdUsuario()>0){
                    membro.setUsuario(usuario);
                }
                usuarios.add(usuario);
            }
            resposta.setValue(usuarios);
        } catch (SQLException ex) {
            Logger.getLogger(DaoUsuario.class.getName()).log(Level.SEVERE, null, ex);
            resposta.setValue(null);
            resposta.addItemLaudo(ex.getMessage());
        }
        return resposta;
    }
    
    public static void main(String[] args) {
        DaoMembroProjetoImpl dao = new DaoMembroProjetoImpl();
        dao.buscarUsuarios();
    }

    @Override
    public Resposta<List<Usuario>> buscarUsuariosNaoMembrosPorProjeto(long idProjeto, String usuarioPesquisado) {
        String sql = "select u.* from Usuario u where u.nome like ? and u.id not in (select m.idUsuario from Membro_Projeto m where m.idProjeto = ?)";
        Resposta resposta = new Resposta();
        try {
            PreparedStatement pst;
            pst = Conexao.getConnection().prepareStatement(sql);
            pst.setString(1, usuarioPesquisado + "%");
            pst.setLong(2, idProjeto); 
            ResultSet rs = pst.executeQuery();
            List<Usuario> usuarios = new ArrayList();
            while(rs.next()){
                Usuario usuario = Util.populaObjeto(Usuario.class, rs);
                usuarios.add(usuario);
            }
            resposta.setValue(usuarios);
        } catch (SQLException ex) {
            Logger.getLogger(DaoUsuario.class.getName()).log(Level.SEVERE, null, ex);
            resposta.setValue(null);
            resposta.addItemLaudo(ex.getMessage());
        }
        return resposta;
    }

    @Override
    public Resposta<List<MembroProjeto>> buscarMembrosPorProjeto(long idProjeto) {
        String sql = "select m.*, u.* from MEMBRO_PROJETO m inner join USUARIO u "
                + "on m.IDUSUARIO=u.ID where m.IDPROJETO = ? ORDER by m.IDUSUARIO";
        Resposta resposta = new Resposta();
        try {
            PreparedStatement pst;
            pst = Conexao.getConnection().prepareStatement(sql);
            pst.setLong(1, idProjeto);
            ResultSet rs = pst.executeQuery();
            List<MembroProjeto> membros = new ArrayList();
            while(rs.next()){
                Usuario usuario = Util.populaObjeto(Usuario.class, rs);
                MembroProjeto membro = Util.populaObjeto(MembroProjeto.class, rs);
                membro.setUsuario(usuario);
                membros.add(membro);
            }
            resposta.setValue(membros);
        } catch (SQLException ex) {
            Logger.getLogger(DaoUsuario.class.getName()).log(Level.SEVERE, null, ex);
            resposta.setValue(null);
            resposta.addItemLaudo(ex.getMessage());
        }
        return resposta;
    }

    @Override
    public List<MembroProjeto> adicionarMembrosProjeto(List<MembroProjeto> membros) throws SQLException {
        Connection con = null;
        try {
            String sql = "insert into MEMBRO_PROJETO (IDUSUARIO, IDPROJETO, PAPEL) values (?, ?, ?)";
            
            con = Conexao.getConnection();
            PreparedStatement pst;
            pst = Conexao.getConnection().prepareStatement(sql);
            for (MembroProjeto membro : membros) {
                pst.setLong(1, membro.getIdUsuario());
                pst.setLong(2, membro.getIdProjeto());
                pst.setString(3, membro.getPapel());
                pst.execute();
            }
            con.commit();
        } finally{
            if(!con.isClosed())
                con.close();
        }
        return null;
    }

}
