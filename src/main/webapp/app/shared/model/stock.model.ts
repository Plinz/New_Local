import { Moment } from 'moment';
import { IImage } from 'app/shared/model//image.model';
import { IProductType } from 'app/shared/model//product-type.model';
import { IHolding } from 'app/shared/model//holding.model';
import { IUser } from 'app/core/user/user.model';
import { IWarehouse } from 'app/shared/model//warehouse.model';

export interface IStock {
    id?: number;
    name?: string;
    description?: string;
    quantityInit?: number;
    quantityRemaining?: number;
    priceUnit?: number;
    onSaleDate?: Moment;
    expiryDate?: Moment;
    bio?: boolean;
    available?: boolean;
    image?: IImage;
    productType?: IProductType;
    holding?: IHolding;
    seller?: IUser;
    warehouse?: IWarehouse;
}

export class Stock implements IStock {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public quantityInit?: number,
        public quantityRemaining?: number,
        public priceUnit?: number,
        public onSaleDate?: Moment,
        public expiryDate?: Moment,
        public bio?: boolean,
        public available?: boolean,
        public image?: IImage,
        public productType?: IProductType,
        public holding?: IHolding,
        public seller?: IUser,
        public warehouse?: IWarehouse
    ) {
        this.bio = this.bio || false;
        this.available = this.available || false;
    }
}
