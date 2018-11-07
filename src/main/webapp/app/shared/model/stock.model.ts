import { Moment } from 'moment';
import { IProductType } from 'app/shared/model//product-type.model';
import { IPerson } from 'app/shared/model//person.model';

export interface IStock {
    id?: number;
    quantityInit?: number;
    quantityRemaining?: number;
    priceUnit?: number;
    imageContentType?: string;
    image?: any;
    onSaleDate?: Moment;
    expiryDate?: Moment;
    bio?: boolean;
    available?: boolean;
    productType?: IProductType;
    person?: IPerson;
}

export class Stock implements IStock {
    constructor(
        public id?: number,
        public quantityInit?: number,
        public quantityRemaining?: number,
        public priceUnit?: number,
        public imageContentType?: string,
        public image?: any,
        public onSaleDate?: Moment,
        public expiryDate?: Moment,
        public bio?: boolean,
        public available?: boolean,
        public productType?: IProductType,
        public person?: IPerson
    ) {
        this.bio = this.bio || false;
        this.available = this.available || false;
    }
}
