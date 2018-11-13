import { IStock } from 'app/shared/model//stock.model';
import { IUser } from 'app/core/user/user.model';

export interface IPurchasePending {
    id?: number;
    quantity?: number;
    stock?: IStock;
    client?: IUser;
}

export class PurchasePending implements IPurchasePending {
    constructor(public id?: number, public quantity?: number, public stock?: IStock, public client?: IUser) {}
}
