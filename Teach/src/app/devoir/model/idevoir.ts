
export interface IDevoir {
    idDevoir:number,
    typedevoir:String,
    description:String,
    ponderation:number,
    bareme:string,
    dateLimite:Date,
    statut:string,
    pdf?:File,
    sousGroupes:any[];
    maxDocuments: number| null;
}
