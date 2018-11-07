import { Moment } from 'moment';
import { IStock } from 'app/shared/model//stock.model';
import { IPerson } from 'app/shared/model//person.model';

export interface IPurchaseDone {
    id?: number;
    saleDate?: Moment;
    quantity?: number;
    stock?: IStock;
    person?: IPerson;
}

export class PurchaseDone implements IPurchaseDone {
    constructor(public id?: number, public saleDate?: Moment, public quantity?: number, public stock?: IStock, public person?: IPerson) {}
}
