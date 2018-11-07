/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { GradeDetailComponent } from 'app/entities/grade/grade-detail.component';
import { Grade } from 'app/shared/model/grade.model';

describe('Component Tests', () => {
    describe('Grade Management Detail Component', () => {
        let comp: GradeDetailComponent;
        let fixture: ComponentFixture<GradeDetailComponent>;
        const route = ({ data: of({ grade: new Grade(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [GradeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(GradeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GradeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.grade).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
