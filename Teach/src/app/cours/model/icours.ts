export interface ICours {
    idCours: number;
    nom: string;
    coefficient: number;
    credits: number;
    code: string;
    enseignant: {
        id: number;
        nom: string;
        prenom: string;
        email: string;
        motDePasse: string;
        specialite: string | null;
    };
    students: Array<{
        id: number;
        nom: string;
        prenom: string;
        email: string;
        motDePasse: string;
    }>;
    methodeCalcul: string | null;
}