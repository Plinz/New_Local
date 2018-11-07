import { IStock } from 'app/shared/model//stock.model';
import { IPerson } from 'app/shared/model//person.model';

export interface IPurchasePending {
    id?: number;
    quantity?: number;
    stock?: IStock;
    person?: IPerson;
}

export class PurchasePending implements IPurchasePending {
    constructor(public id?: number, public quantity?: number, public stock?: IStock, public person?: IPerson) {}
}
