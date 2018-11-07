import { IUser } from 'app/core/user/user.model';
import { IStock } from 'app/shared/model//stock.model';
import { IGrade } from 'app/shared/model//grade.model';
import { IPurchasePending } from 'app/shared/model//purchase-pending.model';
import { IPurchaseDone } from 'app/shared/model//purchase-done.model';
import { ILocation } from 'app/shared/model//location.model';

export interface IPerson {
    id?: number;
    user?: IUser;
    stocks?: IStock[];
    grades?: IGrade[];
    purchasePendings?: IPurchasePending[];
    purchaseDones?: IPurchaseDone[];
    locations?: ILocation[];
}

export class Person implements IPerson {
    constructor(
        public id?: number,
        public user?: IUser,
        public stocks?: IStock[],
        public grades?: IGrade[],
        public purchasePendings?: IPurchasePending[],
        public purchaseDones?: IPurchaseDone[],
        public locations?: ILocation[]
    ) {}
}
