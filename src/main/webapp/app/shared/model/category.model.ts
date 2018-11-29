import { ICategory } from 'app/shared/model//category.model';
import { IImage } from 'app/shared/model//image.model';

export interface ICategory {
    id?: number;
    name?: string;
    description?: string;
    categoyParent?: ICategory;
    images?: IImage[];
}

export class Category implements ICategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public categoyParent?: ICategory,
        public images?: IImage[]
    ) {}
}
