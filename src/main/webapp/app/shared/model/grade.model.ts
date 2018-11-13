import { IUser } from 'app/core/user/user.model';
import { IProductType } from 'app/shared/model//product-type.model';

export interface IGrade {
    id?: number;
    grade?: number;
    user?: IUser;
    productType?: IProductType;
}

export class Grade implements IGrade {
    constructor(public id?: number, public grade?: number, public user?: IUser, public productType?: IProductType) {}
}
