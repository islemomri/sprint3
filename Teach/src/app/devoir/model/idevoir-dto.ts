import { Typedevoir } from "./typedevoir";

export interface IDevoirDTO {
    typedevoir:String,
    description:String,
    ponderation:number,
    bareme:string,
    dateLimite:Date,
    statut:String,
    pdf?:File,
    sousGroupes:string;
    maxDocuments: number| null;
}
