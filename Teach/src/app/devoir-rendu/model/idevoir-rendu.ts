export interface IDevoirRendu {
    idDevoirRendu: number;
    pdfs: Uint8Array[];
    devoir: {
      idDevoir: number;
      typedevoir: string;
      description: string;
      ponderation: number;
      bareme: string;
      dateLimite: string;
      statut: string;
      cours: {
        idCours: number;
        nom: string;
        coefficient: number;
        credits: number;
      };
    };
    etudiant: {
      id: number;
      email: string;
      nom: string;
      prenom: string;
    };
    commentaire: string;
    note: number;
     // Ajoutez cette propriété
  }