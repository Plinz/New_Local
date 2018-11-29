import { ICategory } from 'app/shared/model//category.model';
import { IImage } from 'app/shared/model//image.model';

export interface IProductType {
    id?: number;
    name?: string;
    description?: string;
    category?: ICategory;
    images?: IImage[];
}

export class ProductType implements IProductType {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public category?: ICategory,
        public images?: IImage[]
    ) {}
}
