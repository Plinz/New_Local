import { IUser } from 'app/core/user/user.model';
import { IProductType } from 'app/shared/model//product-type.model';

export interface IGrade {
    id?: number;
    grade?: number;
    nbVoter?: number;
    seller?: IUser;
    productType?: IProductType;
}

export class Grade implements IGrade {
    constructor(
        public id?: number,
        public grade?: number,
        public nbVoter?: number,
        public seller?: IUser,
        public productType?: IProductType
    ) {}
}
