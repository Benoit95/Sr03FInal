package beans;

import java.sql.Timestamp;

public class Utilisateur{
	private long id;
	private boolean admin;
	private String mail;
	private String mdp;
	private String nom;
	private String prenom;
	private String societe;
	private String tel;
	private Timestamp date_crea;
	private String statut;
	
	//getter
	public long getId() {return this.id;}
	public boolean getAdmin() {return this.admin;}
	public String getMail() {return this.mail;}
	public String getMdp() {return this.mdp;}
	public String getNom() {return this.nom;}
	public String getPrenom() {return this.prenom;}
	public String getSociete() {return this.societe;}
	public String getTel() {return this.tel;}
	public Timestamp getDate_crea() {return this.date_crea;}
	public String getStatut() {return this.statut;}
	
	//setter
	public void setId( long l ) {this.id = l;}
	public void setAdmin( boolean admin ) {this.admin = admin;}
	public void setMail( String mail ) {this.mail = mail;}
	public void setMdp( String mdp ) {this.mdp = mdp;}
	public void setNom( String nom ) {this.nom = nom;}
	public void setPrenom( String prenom ) {this.prenom = prenom;}
	public void setSociete( String societe ) {this.societe = societe;}
	public void setTel( String tel ) {this.tel = tel;}
	public void setDate_crea( Timestamp timestamp ) {this.date_crea = timestamp;}
	public void setStatut( String statut ) {this.statut = statut;}
}
