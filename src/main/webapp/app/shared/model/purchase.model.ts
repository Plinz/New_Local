import { Moment } from 'moment';
import { IStock } from 'app/shared/model//stock.model';
import { IUser } from 'app/core/user/user.model';

export interface IPurchase {
    id?: number;
    saleDate?: Moment;
    quantity?: number;
    stock?: IStock;
    client?: IUser;
}

export class Purchase implements IPurchase {
    constructor(public id?: number, public saleDate?: Moment, public quantity?: number, public stock?: IStock, public client?: IUser) {}
}