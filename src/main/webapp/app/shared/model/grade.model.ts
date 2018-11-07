import { IPerson } from 'app/shared/model//person.model';
import { IProductType } from 'app/shared/model//product-type.model';

export interface IGrade {
    id?: number;
    grade?: number;
    person?: IPerson;
    productType?: IProductType;
}

export class Grade implements IGrade {
    constructor(public id?: number, public grade?: number, public person?: IPerson, public productType?: IProductType) {}
}
